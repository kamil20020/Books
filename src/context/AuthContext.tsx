import { createContext, useContext, useEffect, useState } from "react";
import Role from "../models/api/response/role";
import { Login } from "../models/api/request/login";
import AuthService from "../services/AuthService";
import axios from "axios";
import Tokens from "../models/api/response/tokens";
import { NotificationStatus, useNotificationContext } from "./NotificationContext";
import { unstable_batchedUpdates } from "react-dom";
import User from "../models/api/response/user";

enum RoleName{
    ADMIN="ADMIN"
}

interface StorageProps{
    user: User | null,
    tokens: Tokens,
    isUserLogged: boolean
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

    const initUser = (rawUser: string) => {

        const storageUser: StorageProps = JSON.parse(rawUser)

        setUser(storageUser.user)
        setIsUserLogged(storageUser.isUserLogged)
    }

    const initTokens = (rawTokens: string) => {

        const tokens: Tokens = JSON.parse(rawTokens)

        AuthService.configureAuthHeader(tokens.accessToken)
    }

    useEffect(() => {

        const rawStorageUser = localStorage.getItem("user")

        if(!rawStorageUser){
            return
        }

        const rawStorageTokens = localStorage.getItem("tokens") as string

        initUser(rawStorageUser)
        initTokens(rawStorageTokens)
    }, [])

    const login = (request: Login): Promise<string> => {

        return new Promise((resolve, reject) => {

            AuthService.login(request)
            .then((response) => {
    
                const tokens: Tokens = response.data

                const userData: User = AuthService.extractUserFromAccessToken(tokens.accessToken)
                
                setUser(userData)
                setIsUserLogged(true)
    
                const storageUser: StorageProps = {
                    user: userData,
                    isUserLogged: true,
                    tokens: tokens
                }
        
                localStorage.setItem("user", JSON.stringify(storageUser))
                localStorage.setItem("tokens", JSON.stringify(tokens))

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

        return new Promise((resolve, reject) => {

            AuthService.logout()
            .then((response) => {
    
                console.log(response.data)
                
                setUser(null)
                setIsUserLogged(false)
    
                localStorage.removeItem("user")
                localStorage.removeItem("tokens")

                resolve("Success")
            })
            .catch((error) => {
                console.log(error)

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