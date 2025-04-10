const Icon = (props: {
    iconName: string,
    className?: string,
    isDisabled?: boolean,
    size?: number,
    style?: React.CSSProperties,
    onClick?: () => void
}) => {

    const handleClick = () => {

        if(!props.isDisabled && props.onClick){
            props.onClick!()
        }
    }

    return (
        <span 
            className={`material-symbols-outlined ${props.className} ${props.isDisabled && 'disabled-icon'}`}
            onClick={handleClick}
            style={{fontSize: props.size ? props.size : 22, ...props.style, zIndex: 0}}
        >
            {props.iconName}
        </span>
    )
}

export default Icon;