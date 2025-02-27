import AddButton from "../../components/AddButton"
import RemoveButton from "../../components/RemoveButton";
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext";
import Role from "../../models/api/response/role";
import RoleService from "../../services/RoleService";
import UserService from "../../services/UserService";

const RemoveRole = (props: {
    userId: string,
    roleId: string,
    removeRole: (roleId: string) => void;
}) => {

    const setNotification = useNotificationContext().setNotification

    const handleRemoveRole = () => {

        UserService.deleteUserRole(props.userId, props.roleId)
        .then((response) => {

            props.removeRole(props.roleId)

            setNotification({
                message: "Usunięto rolę",
                status: NotificationStatus.SUCCESS
            })
        })
        .catch((error) => {
            console.log(error.response)
        })
    }

    return (
        <RemoveButton 
            dialogTitle="Usuwanie roli"
            onClick={handleRemoveRole}
            style={{marginLeft: 12}}
        />
    )
}

export default RemoveRole;