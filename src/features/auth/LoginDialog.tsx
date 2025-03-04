import Dialog from "../../components/Dialog";
import Login from "./Login";

const LoginDialog = (props: {
    isOpened: boolean,
    onClose: () => void
}) => {

    return (
        <div className="login">
            <Dialog
                title="Logowanie"
                isOpened={props.isOpened}
                content={
                    <Login/>
                }
                onClose={props.onClose}
            />
        </div>
    )
}

export default LoginDialog;