import { createContext, useContext, useState } from "react";
import Role from "../models/api/response/role";
import { Login } from "../models/api/request/login";
import AuthService from "../services/AuthService";

interface AuthProps {
    userId: string,
    username: string,
    roles: Role[]
}

interface AuthContextType{
    user: AuthProps | null,
    isUserLogged: () => boolean,
    login: (user: AuthProps) => void,
    logout: ()  => void
}

const AuthContext = createContext<AuthContextType | null>(null)

export const AuthContextProvider = (props: {
    content: React.ReactNode
}) => {

    const [user, setUser] = useState<AuthProps | null>(null)

    const isUserLogged = () : boolean => {

        return user != null
    }

    const login = (user: AuthProps) => {
        
        setUser(user)
    }

    const logout = () => {

        setUser(null)
    }

    return (
        <AuthContext.Provider value={{user, isUserLogged, login, logout}}>
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