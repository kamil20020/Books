import { createContext, useContext, useEffect, useState } from "react";
import Role from "../models/api/response/role";
import { Login } from "../models/api/request/login";
import AuthService from "../services/AuthService";
import axios from "axios";
import Tokens from "../models/api/response/tokens";
import { NotificationStatus, useNotificationContext } from "./NotificationContext";
import { unstable_batchedUpdates } from "react-dom";
import User from "../models/api/response/user";
import { r } from "react-router/dist/development/fog-of-war-Ckdfl79L";

const getTokensFromStorage = (): Tokens => {

    const rawTokens = localStorage.getItem("tokens") as string

    if(!rawTokens){
        throw new Error("Tokens are not set in storage")
    }

    return JSON.parse(rawTokens)
}

const clearStorage = () => {

    localStorage.removeItem("user")
    localStorage.removeItem("tokens")
}

const setTokensToStorage = (tokens: Tokens) => {

    const encodedTokens = JSON.stringify(tokens)

    localStorage.setItem("tokens", encodedTokens)
}

const isStorageClear = (): boolean => {

    return localStorage.getItem("tokens") == null
}

const handleUnauthorizedResponse = () => {

    if(isStorageClear()){
        return;
    }

    const tokens = getTokensFromStorage()
    const refreshToken = tokens.refreshToken

    AuthService.refreshAccessToken(refreshToken)
    .then((response) => {

        console.log(response.data)

        const tokens: Tokens = response.data
        
        setTokensToStorage(tokens)
    })
    .catch((error) => {

        console.log(error)

        if(error.status == 401){
            clearStorage()
        }
    })
}

axios.interceptors.response.use((response) => {

    return response

}, (error) => {

    console.log(error)

    if(error.status = 401){

        handleUnauthorizedResponse()
    }

    return Promise.reject(error)
})

enum RoleName{
    ADMIN="ADMIN"
}

interface AuthContextType{
    user: User | null,
    isUserLogged: boolean,
    isUserAdmin: () => boolean | undefined,
    login: (credentials: Login) => Promise<string>,
    logout: () => Promise<string>
}

const AuthContext = createContext<AuthContextType | null>(null)

export const AuthProvider = (props: {
    content: React.ReactNode
}) => {

    const [user, setUser] = useState<User | null>(null)
    const [isUserLogged, setIsUserLogged] = useState<boolean>(false)

    useEffect(() => {

        if(isStorageClear()){

            if(isUserLogged){
                clearUser()
            }

            return;
        }

        const tokens: Tokens = getTokensFromStorage()
        const userData: User = AuthService.extractUserFromAccessToken(tokens.accessToken)

        handleLoggedUser(userData, tokens)

    }, [localStorage])

    const clearUser = () => {

        setUser(null)
        setIsUserLogged(false)
    }

    const handleLoggedUser = (user: User, tokens: Tokens) => {

        setUser(user)
        setIsUserLogged(true)

        AuthService.configureAuthHeader(tokens.accessToken)
    }

    const login = (request: Login): Promise<string> => {

        return new Promise((resolve, reject) => {

            AuthService.login(request)
            .then((response) => {
    
                const tokens: Tokens = response.data
                const userData: User = AuthService.extractUserFromAccessToken(tokens.accessToken)

                handleLoggedUser(userData, tokens)

                setTokensToStorage(tokens)

                resolve("Success")
            })
            .catch((error) => {
                console.log(error)

                reject(error.response.data)
            })
        })
    }

    const logout = (): Promise<string> => {

        if(!isUserLogged){
            throw new Error("User is not logged")
        }

        clearUser()
        clearStorage()

        return new Promise((resolve, reject) => {

            AuthService.logout()
            .then((response) => {
    
                console.log(response.data)
                
                AuthService.configureAuthHeader(null)

                resolve("Success")
            })
            .catch((error) => {
                console.log(error)

                if(error.status == 401){

                    console.log(error)

                    AuthService.configureAuthHeader(null)

                    resolve("Success")
                }

                reject(error.response.data)
            })
        })
    }

    const isUserAdmin = (): boolean | undefined => {

        if(!isUserLogged){
            return false
        }

        return user?.roles
            .map((role: Role) => role.name)
            .includes(RoleName.ADMIN)
    }

    return (
        <AuthContext.Provider value={{user: user, isUserLogged: isUserLogged, login, logout, isUserAdmin}}>
            {props.content}
        </AuthContext.Provider>
    )
}

export const useAuthContext = () => {

    const context = useContext(AuthContext)

    if(!context){
        throw new Error("Auth context value is null")
    }

    return context
}