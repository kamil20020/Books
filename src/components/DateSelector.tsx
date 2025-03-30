import { useState } from "react";
import ValidatedInput from "./ValidatedInput";

const DateSelector = (props: {
    value?: Date,
    errorMessage: string;
    onChange: (newValue: Date) => void;
}) => {

    const [isFocused, setIsFocused] = useState<boolean>(false)

    return (
        <ValidatedInput
            inputProps={{
                placeholder: "Data wydania",
                type: isFocused ? "date" : "text",
                onFocus: () => setIsFocused(true),
                onBlur: () => setIsFocused(false),
                value: props.value?.toLocaleDateString()
            }}
            errorMessage={props.errorMessage}
            onChange={(newValue: string) => props.onChange(new Date(newValue))}
        />
    )
}

export default DateSelector;