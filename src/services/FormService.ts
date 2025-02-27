class FormService{

    isRequiredMessage = "Pole nie może by puste"

    validateRequired(value?: string){

        return value && value.trim().length > 0
    }

}

export default new FormService()