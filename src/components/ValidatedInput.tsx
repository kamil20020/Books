const ValidatedInput = (
    props: {
        errorMessage?: string;
        inputProps?: React.DetailedHTMLProps<React.InputHTMLAttributes<HTMLInputElement>, HTMLInputElement>,
        onChange: (newValue: string) => void;
    },
) => {

    const handleOnChange = (event: any) => {

        const newValue = event.target.value

        props.onChange(newValue)
    }

    return (
        <div className="validated-input">
            <input 
                {...props.inputProps}
                onChange={handleOnChange}
            />
            <div className="input-error">
                {props.errorMessage} {props.errorMessage !== undefined && 
                    <div style={{color: "white", display: "inline"}}>
                        white
                    </div>
                }
            </div>
        </div>
    )
}

export default ValidatedInput;