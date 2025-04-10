import { useState } from "react";
import AddButton from "../../components/AddButton";
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext";
import CreateRole from "../../models/api/request/createRole";
import Role from "../../models/api/response/role";
import RoleService from "../../services/RoleService";
import FormService from "../../services/FormService";
import ValidatedInput from "../../components/ValidatedInput";

interface FormProps{
    name: string
}

const AddRole = (props: {
    onAdd: (newRole: Role) => void
}) => {

    const initialForm: FormProps = {
        name: ""
    }

    const [form, setForm] = useState<FormProps>({...initialForm})
    const [errors, setErrors] = useState<FormProps>({...initialForm})

    const setNotification = useNotificationContext().setNotification

    const handleAdd = () => {

        if(!validateForm()){
            throw new Error('Niepoprawny formularz')
        }

        const request: CreateRole = {
            name: form.name
        }

        RoleService.create(request)
        .then((response) => {

            const createdRole = response.data

            props.onAdd(createdRole)

            setNotification({
                message: "Dodano rolę",
                status: NotificationStatus.SUCCESS
            })
        })
        .catch((error) => {

            console.log(error)

            if(error.response.status == 409){

                setNotification({
                    message: "Istnieje już rola o takiej nazwie",
                    status: NotificationStatus.ERROR
                })
            }
        })
    }

    const validateForm = (): boolean => {

        let formPassed = true;

        const newErrors = {...initialForm}

        if(!FormService.validateRequired(form.name)){
            newErrors.name = FormService.isRequiredMessage
            formPassed = false;
        }

        setErrors(newErrors)

        return formPassed
    }

    return (
        <AddButton
            title="Dodaj rolę"
            showDialog
            dialogTitle="Dodawanie roli"
            dialogContent={
                <div className="add-role">
                    <ValidatedInput
                        inputProps={{
                            type: "text",
                            placeholder: "Nazwa"
                        }}
                        errorMessage={errors.name} 
                        onChange={(newRoleName: string) => {
                            setForm({...form, name: newRoleName})
                            setErrors({...initialForm})
                        }}
                    />
                </div>
            }
            onClick={handleAdd}
        />
    )
}

export default AddRole;