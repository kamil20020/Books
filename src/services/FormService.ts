class FormService{

    isRequiredMessage = "Pole nie może by puste"
    isPositiveNumberMessage = "Wartość nie może być mniejsza od zera"
    isMaxPresentDate = "Data nie może być późniejsza niż teraźniejszość"

    validateRequired(value?: string){

        return value != undefined && value.trim().length > 0
    }

    validateRequiredObj(value?: any){

        return value
    }

    validateRequiredArray(value?: any []){

        return value != undefined && value.length > 0
    }

    validatePositiveNumber(value: number){

        return value && value >= 0
    }

    validateMaxPresentDate(value: Date){

        return value <= new Date()
    }
}

export default new FormService()