import { createContext, useContext, useState } from "react";
import Icon from "../components/Icon";


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

const NOTIFICATION_TIME = 4000

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

    const getNotificationStatusClassName = () => {

        if(notification?.status === NotificationStatus.SUCCESS){
            return "notification-success"
        }

        return "notification-error"
    }

    const getNotificationStatusIconName = () => {

        if(notification?.status === NotificationStatus.SUCCESS){
            return "check_circle"
        }

        return "cancel"
    }

    return (
        <NotificationContext.Provider value={{notification, setNotification: handleSetNotification}}>
            {props.content}
            {notification &&
                <div 
                    className={`notification ${getNotificationStatusClassName()}`}
                >
                    <Icon
                        className="notification-icon"
                        iconName={getNotificationStatusIconName()}
                        style={{backgroundColor: "transparent", marginRight: 4}}
                    />
                    {notification.message}
                </div>
            }
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