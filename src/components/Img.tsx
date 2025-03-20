const Img = (props: {
    data?: string
}) => {

    if(!props.data){
        return <div style={{backgroundColor: "teal", width: 100, height: 120}}></div>
    }

    return (
        <img src={`data:image/png;base64,${props.data}`}/>
    )
}

export default Img;