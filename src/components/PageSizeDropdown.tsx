import Dropdown, { Option } from "./Dropdown";

const getPageSizeOption = (pageSize: string): Option => {

    return {
        key: pageSize,
        value: pageSize,
        placeholder: pageSize
    }
}

const options: Option[] = [

    getPageSizeOption("1"),
    getPageSizeOption("2"),
    getPageSizeOption("5"),
    getPageSizeOption("10"),
    getPageSizeOption("25"),
    getPageSizeOption("50"),
    getPageSizeOption("100"),
    getPageSizeOption("200"),

]

const PageSizeDropdown = (props: {
    defaultValue?: number,
    onSelect: (selectedValue: string) => void
}) => {

    return (
        <Dropdown
            options={options}
            defaultValue={props.defaultValue}
            onSelect={props.onSelect}
        />
    )
}

export default PageSizeDropdown;