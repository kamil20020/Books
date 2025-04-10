import { useEffect } from "react"
import { NotificationStatus, useNotificationContext } from "../context/NotificationContext"
import Icon from "./Icon"

const NotificationView = () => {

    const notificationContext = useNotificationContext()
    const notification = notificationContext.notification

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

    if(!notification){
        return <></>
    }

    return (
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
    )
}

export default NotificationView