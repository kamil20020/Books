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
    const isUserAdmin = authContext.isUserAdmin()
    const isUserLogged = authContext.isUserLogged || isUserAdmin

    const isRequiredLogin = props.isRequiredLogin || props.isRequiredAdmin

    if(isRequiredLogin && !isUserLogged){

        return (
            <Login shouldShowTitle/>
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