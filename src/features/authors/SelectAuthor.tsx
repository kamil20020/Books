import { AxiosResponse } from "axios"
import { Option } from "../../components/Dropdown"
import PaginationAPIDropdown from "../../components/PaginationAPIDropdown"
import Pageable from "../../models/api/request/pageable"
import AuthorService from "../../services/AuthorService"
import Author from "../../models/api/response/author"

const SelectAuthor = (props: {
    isMultiSelect?: boolean,
    isRequired?: boolean;
    value?: string[]
    errorMessage?: string;
    handleSelect?: (authorId: string) => void;
    handleMultiSelect?: (authorIds: string[]) => void;
}) => {

    const getOptions = (): Promise<AxiosResponse<any, any>> => {

        const pagination: Pageable = {
            page: 0,
            size: 2
        }

        return AuthorService.getPage(pagination)
    }

    const mapRowToOption = (data: Author): Option => {

        return {
            key: data.id,
            value: data.id,
            placeholder: `${data.firstname} ${data.surname}` 
        }
    }

    return (
        <PaginationAPIDropdown
            key="author"
            title="Autor"
            value={props.value}
            isRequired={props.isRequired}
            errorMessage={props.errorMessage}
            isMultiSelect={props.isMultiSelect}
            getOptions={getOptions}
            mapRowToOption={mapRowToOption}
            onSelect={props.handleSelect}
            onSelectMultiple={props.handleMultiSelect}
        />
    )
}

export default SelectAuthor;