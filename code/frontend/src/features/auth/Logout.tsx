import { useAuthContext } from "../../context/AuthContext"
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext"

const Logout = () => {

    const logout = useAuthContext().logout
    const setNotification = useNotificationContext().setNotification

    const handleLogout = () => {

        logout()
        .then(() => {
            setNotification({
                message: "Udało się wylogować",
                status: NotificationStatus.SUCCESS
            })
        })
        .catch((error) => {
            console.log(error)

            setNotification({
                message: "Nie udało się wylogować",
                status: NotificationStatus.ERROR
            })
        })
    }

    return (
        <button 
            className="remove-button"
            onClick={handleLogout}
            style={{margin: 0}}
        >
            Wyloguj
        </button>
    )
}

export default Logout