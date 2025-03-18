import { useState } from "react";
import AddButton from "../../components/AddButton";
import FormService from "../../services/FormService";
import ValidatedInput from "../../components/ValidatedInput";
import Select from "../../components/Dropdown";
import SelectPublisher from "./SelectPublisher";

interface FormProps{
    firstname: string,
    surname: string,
    mainPublisherId?: string
}

const AddAuthor = () => {

    const initialForm: FormProps = {
        firstname: "",
        surname: ""
    }

    const errorInitialForm: FormProps = {
        ...initialForm,
        mainPublisherId: ""
    }

    const [form, setForm] = useState<FormProps>({...initialForm})
    const [errors, setErrors] = useState<FormProps>({...errorInitialForm})

    const handleAddAuthor = () => {

        if(!validateForm()){
            throw new Error("Invalid form")
        }

        console.log("Add author")
    }

    const validateForm = (): boolean => {

        let formPassed = true
        const newErrors: FormProps = {...errorInitialForm}

        if(!FormService.validateRequired(form.firstname)){
            newErrors.firstname = FormService.isRequiredMessage
            formPassed = false
        }

        if(!FormService.validateRequired(form.surname)){
            newErrors.surname = FormService.isRequiredMessage
            formPassed = false
        }

        setErrors(newErrors)

        return formPassed
    }

    return (
        <AddButton
            showDialog
            title="Dodaj autora"
            dialogTitle="Dodawanie autora"
            dialogContent={
                <div className="add-author">
                    <ValidatedInput
                        inputProps={{
                            placeholder: "Imię"
                        }}
                        errorMessage={errors.firstname} 
                        onChange={(newValue: string) => {
                            setForm({...form, firstname: newValue})
                            setErrors({...errors, firstname: ""})
                        }}
                    />
                    <ValidatedInput
                        inputProps={{
                            placeholder: "Nazwisko"
                        }}
                        errorMessage={errors.surname} 
                        onChange={(newValue: string) => {
                            setForm({...form, surname: newValue})
                            setErrors({...errors, surname: ""})
                        }}
                    />
                    <SelectPublisher
                        selectPublisherId={(newPublisherId) => {
                            setForm({...form, mainPublisherId: newPublisherId})
                        }}
                    />
                </div>
            }
            onClick={handleAddAuthor}            
        />
    )
}

export default AddAuthor;