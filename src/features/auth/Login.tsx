import { useState } from "react";
import ConfirmationDialog from "../../components/ConfirmationDialog";
import Dialog from "../../components/Dialog";
import ValidatedInput from "../../components/ValidatedInput";
import { Login } from "../../models/api/request/login";
import AddButton from "../../components/AddButton";
import "./auth.css";
import FormService from "../../services/FormService";
import AuthService from "../../services/AuthService";
import { useAuthContext } from "../../context/AuthContext";
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext";

const LoginView = (props: {
    shouldShowTitle?: boolean,
    onAccept?: () => void
}) => {

    const login = useAuthContext().login
    const setNotifcation = useNotificationContext().setNotification

    const [form, setForm] = useState<Login>({
        username: "",
        password: ""
    })

    const initErrors: Login = {
        username: "",
        password: ""
    }

    const [errors, setErrors] = useState<Login>({...initErrors})

    const validateForm = () => {

        let isFormValid = true

        const newErrors: Login = {...initErrors}

        if(!FormService.validateRequired(form.username)){
            newErrors.username = FormService.isRequiredMessage
            isFormValid = false
        }

        if(!FormService.validateRequired(form.password)){
            newErrors.password = FormService.isRequiredMessage
            isFormValid = false
        }

        setErrors(newErrors)

        return isFormValid
    }

    const handleLogin = () => {

        if(!validateForm()){
            return;
        }

        login(form)
        .then(() => {
            setNotifcation({
                message: "Udało się zalogować",
                status: NotificationStatus.SUCCESS
            })

            if(props.onAccept){
                props.onAccept()
            }
        })
        .catch((error) => {
            console.log(error)

            setNotifcation({
                message: "Podane login lub hasło się nie zgadzają",
                status: NotificationStatus.ERROR
            })
        })
    }

    return (
        <div className="login">
            {props.shouldShowTitle &&
                <h2 
                    style={{textAlign: "center"}}
                >
                    Logowanie
                </h2>
            }
           <ValidatedInput 
                inputProps={{
                    type: "text",
                    placeholder: "Login"
                }}
                errorMessage={errors.username}
                onChange={(newValue: string) => {
                    setForm({...form, username: newValue})
                    setErrors({...errors, username: ""})
                }}
            />
            <ValidatedInput 
                inputProps={{
                    type: "password",
                    placeholder: "Hasło"
                }}
                errorMessage={errors.password}
                onChange={(newValue: string) => {
                    setForm({...form, password: newValue})
                    setErrors({...errors, password: ""})
                }}
            />
            <AddButton
                title="Zaloguj"
                style={{justifySelf: "center"}}
                onClick={handleLogin}
            />
        </div>
    )
}

export default LoginView;