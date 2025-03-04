import { useState } from "react";
import ConfirmationDialog from "../../components/ConfirmationDialog";
import Dialog from "../../components/Dialog";

const Login = () => {

    const handleLogin = () => {

        console.log("Login")
    }

    return (
        <div className="login">
            Logowanie
        </div>
    )
}

export default Login;