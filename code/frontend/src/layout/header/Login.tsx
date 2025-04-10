import { useState } from "react"
import LoginDialog from "../../features/auth/LoginDialog"

const LoginView = () => {

    const [isOpened, setIsOpened] = useState<boolean>(false)

    const handleDialogAppearance = () => {

        setIsOpened(!isOpened)
    }

    const onClose = () => {

        setIsOpened(false)
    }

    return (
        <div className="header-login">
            <button 
                className="add-button"
                onClick={handleDialogAppearance}
                style={{margin: 0}}
            >
                Logowanie
            </button>
            <LoginDialog
                isOpened={isOpened}
                onClose={onClose}
            />
        </div>
    )
}

export default LoginView;