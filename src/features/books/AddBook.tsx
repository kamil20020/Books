import { useState } from "react";
import AddButton from "../../components/AddButton";
import ValidatedInput from "../../components/ValidatedInput";
import Author from "../../models/api/response/author";
import Publisher from "../../models/api/response/publisher";
import CreateBook from "../../models/api/request/createBook";
import { an } from "react-router/dist/development/route-data-Cw8htKcF";
import DateSelector from "../../components/DateSelector";
import SelectPublisher from "../publisher/SelectPublisher";
import SelectAuthor from "../authors/SelectAuthor";
import FormService from "../../services/FormService";
import BookService from "../../services/BookService";
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext";

interface ErrorsProps{
    title: string;
    publicationDate: string;
    price: string;
    publisherId: string;
    authorsIds: string
}

const AddBook = () => {

    const initForm: CreateBook = {
        title: "",
        publicationDate: new Date(),
        price: 0,
        publisherId: "",
        authorsIds: []
    }

    const initErrors: ErrorsProps = {
        title: "",
        publicationDate: "",
        price: "",
        publisherId: "",
        authorsIds: "",
    }

    const [form, setForm] = useState<CreateBook>({...initForm})
    const [errors, setErrors] = useState<ErrorsProps>({...initErrors})

    const setNotification = useNotificationContext().setNotification

    const handleChangeForm = (formFieldName: string, newValue: any) => {

        setForm({...form, [formFieldName]: newValue})
        setErrors({...errors, [formFieldName]: ""})
    }

    const handleSaveBook = () => {

        if(!handleValidateForm()){
            throw new Error("Niepoprawne dane")
        }

        BookService.create(form)
        .then((response) => {

            console.log(response.data)

            setNotification({
                message: "Utworzono książkę",
                status: NotificationStatus.SUCCESS
            })
        })
        .catch((error) => {

            console.log(error)

            setNotification({
                message: "Wystąpił błąd",
                status: NotificationStatus.ERROR
            })
        })
    }

    const handleValidateForm = (): boolean => {

        let isFormValid = true;
        const newErrors = {...initErrors}

        if(!FormService.validateRequired(form.title)){
            newErrors.title = FormService.isRequiredMessage
            isFormValid = false;
        }

        if(!FormService.validateMaxPresentDate(form.publicationDate)){
            newErrors.publicationDate = FormService.isMaxPresentDate
            isFormValid = false;
        }

        if(!FormService.validateRequiredObj(form.price)){
            newErrors.price = FormService.isRequiredMessage
            isFormValid = false;
        }

        if(!FormService.validateRequired(form.publisherId)){
            newErrors.publisherId = FormService.isRequiredMessage
            isFormValid = false;
        }

        if(!FormService.validateRequiredArray(form.authorsIds)){
            newErrors.authorsIds = FormService.isRequiredMessage
            isFormValid = false;
        }

        setErrors(newErrors)

        return isFormValid
    }

    return (
        <AddButton
            showDialog
            title="Dodaj książkę"
            dialogTitle="Dodawanie książki"
            style={{alignSelf: "start"}}
            dialogContent={
                <div className="add-book">
                    <ValidatedInput
                        inputProps={{
                            placeholder: "Tytuł"
                        }}
                        errorMessage={errors.title}
                        onChange={(newValue) => handleChangeForm("title", newValue)}
                    />
                    <DateSelector
                        value={form.publicationDate}
                        errorMessage={errors.publicationDate}
                        onChange={(newValue) => handleChangeForm("publicationDate", newValue)}
                    />
                    <ValidatedInput
                        inputProps={{
                            placeholder: "Cena zł",
                            type: "number",
                            min: 0,
                            step: 0.01
                        }}
                        errorMessage={errors.price}
                        onChange={(newValue) => handleChangeForm("price", newValue)}
                    />
                    <SelectPublisher
                        isRequired
                        value={[form.publisherId]}
                        errorMessage={errors.publisherId}
                        selectPublisherId={(newValue: string) => handleChangeForm("publisherId", newValue)}
                    />
                    <SelectAuthor
                        isRequired
                        value={form.authorsIds}
                        isMultiSelect
                        errorMessage={errors.authorsIds}
                        handleMultiSelect={(newValues: string[]) => handleChangeForm("authorsIds", newValues)}
                    />
                    Dodaj obraz
                </div>
            }
            onClick={handleSaveBook}
        />
    )
}

export default AddBook;