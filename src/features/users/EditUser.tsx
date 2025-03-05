import { useState } from "react";
import AddButton from "../../components/AddButton";
import ValidatedInput from "../../components/ValidatedInput";
import User from "../../models/api/response/user";
import FormService from "../../services/FormService";
import UserService from "../../services/UserService";
import PatchUser from "../../models/api/request/patchUser";
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext";

interface FormProps{
    username: string,
    password: string,
    repeatedPassword: string
}

const EditUser = (props: {
    user: User,
    editUser: (newData: User) => void
}) => {

    const user = props.user

    const [form, setForm] = useState<FormProps>({
        username: user.username,
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

    const handleEditUser = () => {

        if(!validateForm()){
            throw new Error("Niewłaściwe dane formularza")
        }

        const request: PatchUser = {
            username: undefined
        }

        if(form.username !== user.username){
            request.username = form.username
        }

        UserService.patchById(user.id, request)
        .then((response) => {

            const editedUser: User = response.data

            props.editUser(editedUser)

            setForm({
                username: "",
                password: "",
                repeatedPassword: ""
            })

            setNotification({
                message: "Zapisano dane użytkownika",
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

    const validateForm = () => {

        let formPassed = true

        const newErrors: FormProps = {...initErrors}

        if(!FormService.validateRequired(form.username)){
            newErrors.username = FormService.isRequiredMessage
            formPassed = false;
        }

        // if(!FormService.validateRequired(form.password)){
        //     newErrors.password = FormService.isRequiredMessage
        //     formPassed = false;
        // }

        // if(!FormService.validateRequired(form.repeatedPassword)){
        //     newErrors.repeatedPassword = FormService.isRequiredMessage
        //     formPassed = false;
        // }
        // else if(form.password !== form.repeatedPassword){
        //     newErrors.repeatedPassword = "Hasła się różnią"
        //     formPassed = false;
        // }

        setErrors(newErrors)

        return formPassed;
    }

    return (
        <AddButton
            showDialog
            title="Edytuj użytkownika"
            dialogTitle="Edytowanie użytkownika"
            style={{alignSelf: "start", marginTop: 0}}
            dialogContent={
                <div className="edit-user">
                    <ValidatedInput 
                        inputProps={{
                            type: "text",
                            placeholder: "Nazwa użytkownika",
                            value: form.username
                        }}
                        errorMessage={errors.username}
                        onChange={(newValue: string) => {
                            setForm({...form, username: newValue})
                            setErrors({...errors, username: ""})
                        }}                    
                    />
                     {/* <ValidatedInput 
                        inputProps={{
                            type: "password",
                            placeholder: "Hasło"
                        }}
                        errorMessage={errors.password}
                        onChange={(newValue: string) => setForm({...form, password: newValue})}                    
                    />
                     <ValidatedInput 
                        inputProps={{
                            type: "password",
                            placeholder: "Powtórz hasło"
                        }}
                        errorMessage={errors.repeatedPassword}
                        onChange={(newValue: string) => setForm({...form, repeatedPassword: newValue})}                    
                    /> */}
                </div>
            }
            onClick={handleEditUser}
        />
    )
}

export default EditUser;