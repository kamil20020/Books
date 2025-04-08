import { useState } from "react";
import Icon from "./Icon";

const Dialog = (props: {
    title: string;
    isOpened: boolean;
    content: React.ReactNode;
    widthPct?: number;
    heightPct?: number;
    onClose: () => void;
}) => {

    if(!props.isOpened){
        return <></>
    }

    return (
        <div className="dialog-cover">
            <div 
                className="dialog noselect"
            >
                <h2 style={{textAlign: "center"}}>{props.title}</h2>
                <Icon 
                    iconName="close" 
                    size={32} 
                    style={{
                        position: "absolute", 
                        right: 12, 
                        top: 12
                    }}
                    onClick={props.onClose}
                />
                {props.content}
            </div>
        </div>
    )
}

export default Dialog;