import { NotificationStatus, useNotificationContext } from "../context/NotificationContext";
import AddButton from "./AddButton";
import Dialog from "./Dialog"
import RemoveButton from "./RemoveButton";

const ConfirmationDialog = (props: {
    title: string;
    isOpened: boolean;
    content?: React.ReactNode;
    onAccept: () => void;
    onClose: () => void;
}) => {

    const setNotification = useNotificationContext().setNotification

    const handleAccept = () => {

        try{
            props.onAccept()
        }
        catch(error: any){

            console.log(error.message)

            setNotification({
                message: error.message,
                status: NotificationStatus.ERROR
            })

            return;
        }

        props.onClose()
    }

    return (
        <Dialog 
            title={props.title} 
            isOpened={props.isOpened} 
            onClose={props.onClose}
            content={
                <>
                    {props.content}
                    <div className="confirmation-dialog">
                        <button 
                            className="add-button"
                            onClick={handleAccept}
                            style={{margin: 0}}
                        >
                            Akceptuj
                        </button>
                        <button 
                            className="remove-button"
                            onClick={props.onClose}
                            style={{margin: 0}}
                        >
                            Anuluj
                        </button>
                    </div>
                </>
            }
        />
    )
}

export default ConfirmationDialog;