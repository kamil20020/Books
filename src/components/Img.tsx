const Img = (props: {
    data?: string,
    width?: number,
    height?: number,
    maxWidth?: number,
    maxHeight?: number,
    style?: React.CSSProperties
}) => {

    const height = props.height ? props.height : "auto"
    const width = props.width ? props.width : 180
    const maxWidth = props.maxWidth ? props.maxWidth : 500
    const maxHeight = props.maxHeight ? props.maxHeight : 500

    if(!props.data){
        return (
            <div 
                style={{
                    backgroundColor: "teal",
                    width: width,
                    height: height,
                    maxHeight: maxHeight,
                    maxWidth: maxWidth
                }}
            >
            </div>
        )
    }

    return (
        <img 
            src={`data:image/png;base64,${props.data}`}
            height={height}
            width={width}
            style={{
                maxHeight: maxHeight,
                maxWidth: maxWidth
            }}
        />
    )
}

export default Img;