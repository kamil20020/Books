const ContentHeader = (props: {
    title: string
}) => {

    return (
        <h1 style={{marginTop: 12, textAlign: "center"}}>{props.title}</h1>
    )
}

export default ContentHeader;