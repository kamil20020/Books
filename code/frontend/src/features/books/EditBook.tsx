import { useState } from "react";
import AddButton from "../../components/AddButton";
import Book from "../../models/api/response/book";
import PatchBook from "../../models/api/request/patchBook";
import FormService from "../../services/FormService";
import BookService from "../../services/BookService";
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext";
import ValidatedInput from "../../components/ValidatedInput";
import SelectImage from "../../components/SelectImage";

interface EditBookErrors{
    title: string;
    price: string
}

const EditBook = (props: {
    book: Book,
    handleEdit: (updatedBook: Book) => void
}) => {

    const book = props.book

    const [form, setForm] = useState<PatchBook>({
        title: book.title,
        price: book.price,
        picture: book.picture
    })

    const initErrors: EditBookErrors = {
        title: "",
        price: ""
    }

    const [errors, setErrors] = useState<EditBookErrors>({...initErrors})

    const setNotifcation = useNotificationContext().setNotification

    const handleEditBook = () => {

        if(!validateForm()){
            throw new Error("Niepoprawny formularz")
        }

        BookService.patchById(book.id, form)
        .then((response) => {

            props.handleEdit(response.data)

            setNotifcation({
                message: "Zapisano dane książki",
                status: NotificationStatus.SUCCESS
            })
        })
        .catch((error) => {

            setNotifcation({
                message: error.response.data,
                status: NotificationStatus.ERROR
            })
        })
    }

    const validateForm = (): boolean => {

        let formPassed = true
        const newErrors = {...initErrors}

        if(!FormService.validateRequired(form.title)){
            newErrors.title = FormService.isRequiredMessage
            formPassed = false;
        }

        if(!FormService.validatePositiveNumber(form.price)){
            newErrors.price = FormService.isPositiveNumberMessage
            formPassed = false;
        }

        setErrors(newErrors)

        return formPassed
    }

    const handleChange = (name: string, newValue: any) => {

        setForm({...form, [name]: newValue})
        setErrors({...initErrors, [name]: ""})
    }

    return (

        <AddButton
            showDialog
            title="Edycja"
            dialogTitle="Edycja ksiązki"
            dialogContent={
                <div className="edit-book">
                    <ValidatedInput
                        inputProps={{
                            placeholder: "Tytuł"
                        }}
                        value={form.title}
                        errorMessage={errors.title}
                        onChange={(newValue: string) => handleChange("title", newValue)}
                    />
                    <ValidatedInput
                        inputProps={{
                            type: "number",
                            placeholder: "Cena zł",
                            min: 0,
                            step: 0.01
                        }}
                        value={form.price}
                        errorMessage={errors.price}
                        onChange={(newValue: string) => handleChange("price", newValue)}
                    />
                    <SelectImage
                        img={book.picture}
                        onChange={(newValue: string) => handleChange("picture", newValue)}
                    />
                </div>
            }
            onClick={handleEditBook}
        />
    )
}

export default EditBook;