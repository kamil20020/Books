import { useState } from "react";
import ConfirmationDialog from "./ConfirmationDialog";
import { NotificationStatus, useNotificationContext } from "../context/NotificationContext";

const AddButton = (props: {
    title?: string;
    dialogTitle?: string;
    showDialog?: boolean;
    dialogContent?: React.ReactNode;
    style?: React.CSSProperties;
    onClick: () => void;
}) => {

    const [isOpened, setIsOpened] = useState<boolean>(false)

    const handleAdd = () => {

        props.onClick()

        setIsOpened(false)
    }

    return (
        <>
            <button 
                className="add-button"
                style={{...props.style}}
                onClick={() => {

                    if(props.showDialog){

                        setIsOpened(true)
                        return;
                    }

                    props.onClick()
                }}
            >
                {props.title ? props.title : "Dodaj"}
            </button>
            {props.showDialog && 
                <ConfirmationDialog 
                    title={props.dialogTitle!}
                    isOpened={isOpened}
                    onAccept={handleAdd}
                    content={props.dialogContent}
                    onClose={() => setIsOpened(false)} 
                />
            }
        </>
    )
}

export default AddButton;