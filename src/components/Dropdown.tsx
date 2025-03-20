export interface Option{
    key: string,
    value: any,
    placeholder: string | React.ReactNode
}

const Dropdown = (props: {
    options: Option[],
    defaultValue?: any,
    style?: React.DetailedHTMLProps<React.InputHTMLAttributes<HTMLInputElement>, HTMLInputElement>,
    onSelect: (selectedValue: string) => void;
}) => {

    const handleSelectOption = (selectedOptionIndex: number) => {

        const selectedOptionValue = props.options[selectedOptionIndex].value

        props.onSelect(selectedOptionValue)
    }

    return (
        <select
            className="dropdown" 
            style={{
                padding: "6px 14px",
                overflowX: "auto",
                ...props.style
            }}
            defaultValue={props.defaultValue}
            onChange={(event) => handleSelectOption(event.target.selectedIndex)}
        >
            {props.options.map((option: Option) => (
                <option 
                    key={option.key} 
                    value={option.value}
                >
                    {option.placeholder}
                </option>
            ))}
        </select>
    )
}

export default Dropdown;