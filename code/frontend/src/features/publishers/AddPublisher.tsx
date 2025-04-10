import { useSearchParams } from "react-router";
import AddButton from "../../components/AddButton";
import ValidatedInput from "../../components/ValidatedInput";
import { useState } from "react";
import FormService from "../../services/FormService";
import CreatePublisher from "../../models/api/request/createPublisher";
import PublisherService from "../../services/PublisherService";
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext";
import Publisher from "../../models/api/response/publisher";

const AddPublisher = (props: {
    onAdd: (newPublisher: Publisher) => void;
}) => {

    const [name, setName] = useState<string>("")
    const [nameError, setNameError] = useState<string>("")

    const setNotification = useNotificationContext().setNotification

    const validateForm = (): boolean => {

        if(!FormService.validateRequired(name)){

            setNameError(FormService.isRequiredMessage)

            return false;
        }

        return true
    }

    const handleSavePublisher = () => {

        if(!validateForm()){
            throw new Error("Niepoprawny formularz");
        }

        const request: CreatePublisher = {
            name: name
        }

        PublisherService.create(request)
        .then((response) => {

            props.onAdd(response.data)

            setNotification({
                message: "Utworzono wydawcę",
                status: NotificationStatus.SUCCESS
            })
        })
        .catch((error) => {

            setNotification({
                message: error.response.data,
                status: NotificationStatus.ERROR
            })
        })
    }

    const onNameChange = (newName: string) => {

        setName(newName)
        setNameError("")
    }

    return (
        <AddButton
            showDialog
            title="Dodaj wydawcę"
            dialogTitle="Dodawanie wydawcy"
            dialogContent={
                <div className="create-publisher">
                    <ValidatedInput
                        inputProps={{
                            placeholder: "Nazwa wydawcy"
                        }}
                        errorMessage={nameError}
                        onChange={onNameChange}
                    />
                </div>
            }
            style={{alignSelf: "start"}}
            onClick={handleSavePublisher}
        />
    )
}

export default AddPublisher;