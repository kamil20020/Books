import { useState } from "react";
import AddButton from "../../components/AddButton";
import ValidatedInput from "../../components/ValidatedInput";
import User from "../../models/api/response/user";
import FormService from "../../services/FormService";
import CreateUser from "../../models/api/request/createUser";
import UserService from "../../services/UserService";
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext";

interface FormProps{
    username: string,
    password: string,
    repeatedPassword: string;
}

const AddUser = (props: {
    addUser: (user: User) => void;
}) => {

    const [form, setForm] = useState<FormProps>({
        username: "",
        password: "",
        repeatedPassword: ""
    })

    const initErrors: FormProps = {
        username: "",
        password: "",
        repeatedPassword: ""
    }

    const [errors, setErrors] = useState<FormProps>({...initErrors})

    const setNotification = useNotificationContext().setNotification

    const handleAddUser = () => {
        
        if(!validateFormAndFillErrors()){
            throw new Error("Formularz niepoprawny");
        }

        const request: CreateUser = {
            username: form.username,
            password: form.password
        }

        UserService.createUser(request)
        .then((response) => {

            const newUser: User = response.data

            props.addUser(newUser)

            setForm({
                username: "",
                password: "",
                repeatedPassword: ""
            })

            setNotification({
                message: "Utworzono użytkownika",
                status: NotificationStatus.SUCCESS
            })
        })
        .catch((error) => {

            if(error.status == 409){
                setErrors({...errors, username: error.response.data})
            }

            setNotification({
                message: error.response.data,
                status: NotificationStatus.ERROR
            })
        })
    }

    const validateFormAndFillErrors = () => {

        let testPassed = true

        const newErrors: FormProps = {...initErrors}

        if(!FormService.validateRequired(form.username)){
            newErrors.username = FormService.isRequiredMessage
            testPassed = false
        }

        if(!FormService.validateRequired(form.password)){
            newErrors.password = FormService.isRequiredMessage
            testPassed = false
        }

        if(!FormService.validateRequired(form.repeatedPassword)){
            newErrors.repeatedPassword = FormService.isRequiredMessage
            testPassed = false
        }
        else if(form.password !== form.repeatedPassword){
            newErrors.repeatedPassword = "Hasła nie są identyczne"
            testPassed = false
        }

        setErrors(newErrors)

        console.log(testPassed)

        return testPassed
    }

    return (
        <AddButton
            showDialog
            title="Dodaj użytkownika"
            dialogTitle="Dodawanie użytkownika"
            style={{alignSelf: "start"}}
            dialogContent={
                <div className="add-user">
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
                    <ValidatedInput 
                        inputProps={{
                            type: "password",
                            placeholder: "Powtórz hasło"
                        }}
                        errorMessage={errors.repeatedPassword}
                        onChange={(newValue: string) => {
                            setForm({...form, repeatedPassword: newValue})
                            setErrors({...errors, repeatedPassword: ""})
                        }}
                    />
                </div>
            }
            onClick={handleAddUser}
        />
    )
}

export default AddUser;