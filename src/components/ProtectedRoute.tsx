import { useState } from "react";
import { useAuthContext } from "../context/AuthContext";
import LoginDialog from "../features/auth/LoginDialog";
import Login from "../features/auth/Login";
import Forbidden from "../error/Forbidden";

const ProtectedRoute = (props: {
    content: React.ReactNode,
    isRequiredLogin?: boolean,
    isRequiredAdmin?: boolean
}) => {

    const authContext = useAuthContext()
    const isUserLogged = authContext.isUserLogged
    const isUserAdmin = authContext.isUserAdmin()

    if(props.isRequiredLogin && !isUserLogged){

        return (
            <Login/>
        )
    }

    if(props.isRequiredAdmin && !isUserAdmin){

        return (
            <Forbidden/>
        )
    }

    return (
       <>
        {props.content}
       </>
    )
}

export default ProtectedRoute;