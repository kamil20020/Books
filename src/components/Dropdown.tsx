import { Y } from "react-router/dist/development/fog-of-war-Ckdfl79L"
import AddButton from "./AddButton"
import { useEffect, useRef, useState } from "react"
import Pageable from "../models/api/request/pageable"
import PublisherService from "../services/PublisherService"
import { AxiosResponse } from "axios"
import Page from "../models/api/response/page"

export interface Option{
    key: string,
    value: string,
    placeholder: string | React.ReactNode
}

const Dropdown = (props: {
    title: string,
    getOptions: (pageable: Pageable) => Promise<AxiosResponse<any, any>>;
    mapRowToOption: (data: any) => Option;
    onSelect: (selectedValue: string) => void;
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

    const handleSelectOption = (globalSelectedValueIndex: number) => {

        if(globalSelectedValueIndex <= 1 || globalSelectedValueIndex == options.length + 2){
            return;
        }

        const optionsIndex = globalSelectedValueIndex - 2
        const selectedValue = options[optionsIndex].value

        props.onSelect(selectedValue)
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
        <select
            className="dropdown" 
            size={5} 
            style={{
                padding: "6px 14px",
                width: "100%",
                overflowX: "auto"
            }}
            onChange={(event) => handleSelectOption(event.target.selectedIndex)}
        >
            <TitleOption/>
            <EmptyOption/>
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
    )
}

export default Dropdown