import { useState } from "react";
import Dialog from "./Dialog";
import ConfirmationDialog from "./ConfirmationDialog";

const RemoveButton = (props: {
    title?: string;
    dialogTitle: string;
    style?: React.CSSProperties
    onClick: () => void;
}) => {

    const [isOpened, setIsOpened] = useState<boolean>(false)

    const handleRemove = () => {

        props.onClick()
        setIsOpened(false)
    }

    return (
        <>
            <button 
                className="remove-button"
                onClick={() => setIsOpened(true)}
                style={{...props.style}}
            >
                {props.title ? props.title : "Usuń"}
            </button>
            <ConfirmationDialog 
                title={props.dialogTitle} 
                isOpened={isOpened}
                onAccept={handleRemove}
                onClose={() => setIsOpened(false)} 
            />
        </>
    )
}

export default RemoveButton;