import { createContext, useContext, useState } from "react";
import Icon from "../components/Icon";
import NotificationView from "../components/Notification";

const NOTIFICATION_TIME = 4000

export enum NotificationStatus{
    SUCCESS, ERROR
};

interface Notification{
    message: string;
    status: NotificationStatus
};

interface NotificationContextType{
    notification: Notification | null,
    setNotification: (notification: Notification) => void;
}

const NotificationContext = createContext<NotificationContextType | null>(null)

export const NotificationProvider = (props: {
    content: React.ReactNode
}) => {

    const [notification, setNotification] = useState<Notification | null>(null)

    const handleSetNotification = (notification: Notification) => {

        setNotification(notification)

        setTimeout(
            () => setNotification(null),
            NOTIFICATION_TIME
        )
    }

    return (
        <NotificationContext.Provider value={{notification, setNotification: handleSetNotification}}>
            {props.content}
            <NotificationView/>
        </NotificationContext.Provider>
    )
}

export const useNotificationContext = () => {

    const context = useContext(NotificationContext)

    if(!context){
        throw new Error("Notification context value is null")
    }

    return context;
}