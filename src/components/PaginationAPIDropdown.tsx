import { Y } from "react-router/dist/development/fog-of-war-Ckdfl79L"
import AddButton from "./AddButton"
import { useEffect, useRef, useState } from "react"
import Pageable from "../models/api/request/pageable"
import PublisherService from "../services/PublisherService"
import { AxiosResponse } from "axios"
import Page from "../models/api/response/page"
import { Option } from "./Dropdown"

const PaginationAPIDropdown = (props: {
    isRequired?: boolean,
    isMultiSelect?: boolean;
    errorMessage?: string;
    value?: string[]
    title: string,
    getOptions: (pageable: Pageable) => Promise<AxiosResponse<any, any>>;
    mapRowToOption: (data: any) => Option;
    onSelect?: (selectedValue: string) => void;
    onSelectMultiple?: (selectedValues: string[]) => void;
}) => {

    const pageSize = 2
    const page = useRef<number>(0)
    const totalElements = useRef<number>(0)

    const [options, setOptions] = useState<Option[]>([])

    useEffect(() => {

        handleSearchAndAppend(0)
    }, [])
 
    const handleSearchAndAppend = (newPage: number) => {

        const pageable: Pageable = {
            page: newPage,
            size: pageSize
        }

        props.getOptions(pageable)
        .then((response) => {

            const pagedResponse: Page<any> = response.data

            const newOptions: Option[] = pagedResponse.content
                .map(props.mapRowToOption)

            setOptions([...options, ...newOptions])
            page.current = newPage
            totalElements.current = pagedResponse.totalElements
        })
    }

    const handleSelectOptions = (selectedOptionsCollection: HTMLCollection) => {

        setTimeout(() => { // to run onClick on option

            let selectedValues: string[] = Array.from(selectedOptionsCollection)
            .filter((option: any) => isIndexStandard(option.index))
            .map((option: any) => getValueFromOption(option))

            if(props.isMultiSelect){
                props.onSelectMultiple!(selectedValues)
            }
            else{
                props.onSelect!(selectedValues[0])
            }
        }, 0)
    }

    const isIndexStandard = (optionIndex: number): boolean => {

        return optionIndex >= getFirstApiOptionIndex() && optionIndex <= getLastApiOptionIndex()
    }

    const getValueFromOption = (option: HTMLOptionElement) => {

        const selectedValueIndex = option.index

        const optionsIndex = getApiOptionIndex(selectedValueIndex)
        const selectedValue = options[optionsIndex].value

        return selectedValue
    }

    const getFirstApiOptionIndex = (): number => {

        return props.isRequired ? 1 : 2 
    }

    const getLastApiOptionIndex = (): number => {

        return getFirstApiOptionIndex() + options.length - 1
    }

    const getApiOptionIndex = (selectedValueIndex: number) => {

        return selectedValueIndex - getFirstApiOptionIndex()
    }

    const TitleOption = () => (

        <option 
            disabled 
            style={{fontWeight: "bold"}}
        >
            {props.title}
        </option>
    )

    const EmptyOption = () => (

        <option
            key={"empty"} 
            value={undefined}
        >
            Brak
        </option>
    )

    const PaginationOption = () => (

        <>
            {options.length < totalElements.current &&
                 <option
                    key={"load"} 
                    value={undefined} 
                    style={{fontWeight: "bold"}} 
                    onClick={() => handleSearchAndAppend(page.current + 1)}
                >
                        Załaduj
                </option>
            }
        </>
    )

    return (
        <div className="pagination-dropdown">
            <select
                className="dropdown"
                multiple={props.isMultiSelect }
                size={5}
                style={{
                    padding: "6px 14px",
                    width: "100%",
                }}
                onChange={(event) => handleSelectOptions(event.target.selectedOptions)}
            >
                <TitleOption/>
                {!props.isRequired && <EmptyOption/>}
                {options.map((option: Option) => (
                    <option 
                        key={option.key} 
                        value={option.value}
                    >
                        {option.placeholder}
                    </option>
                ))}
                <PaginationOption/>
            </select>
            <div className="input-error">
                {props.errorMessage} <div style={{color: "white", display: "inline"}}>white</div>
            </div>
        </div>
    )
}

export default PaginationAPIDropdown