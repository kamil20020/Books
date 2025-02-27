const Content = (props: {
    children: React.ReactNode
}) => {

    return (
        <main>
            <div id="content">
                {props.children}
            </div>
        </main>
    )
}

export default Content;