const Icon = (props: {
    iconName: string,
    className?: string,
    size?: number,
    style?: React.CSSProperties,
    onClick?: () => void
}) => {

    return (
        <span 
            className={`material-symbols-outlined ${props.className}`}
            onClick={props.onClick}
            style={{fontSize: props.size ? props.size : 22, ...props.style}}
        >
            {props.iconName}
        </span>
    )
}

export default Icon;