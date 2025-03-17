import ConfirmationDialog from "../../components/ConfirmationDialog";
import RemoveButton from "../../components/RemoveButton";
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext";
import Role from "../../models/api/response/role";
import RoleService from "../../services/RoleService";

const RemoveRole = (props: {
    role: Role,
    onRemove: (removedRoleId: string) => void
}) => {

    const role = props.role

    const setNotification = useNotificationContext().setNotification

    const handleRemove = () => {

        RoleService.deleteById(role.id)
        .then((response) => {

            props.onRemove(role.id)

            setNotification({
                message: "Usunięto rolę",
                status: NotificationStatus.SUCCESS
            })
        })
        .catch((error) => {
            
            setNotification({
                message: error.response.data,
                status: NotificationStatus.ERROR
            })
        })
    }

    return (
        <RemoveButton
            title="Usuń rolę"
            dialogTitle={`Czy usunąć rolę ${role.name}?`} 
            onClick={handleRemove}  
        />
    )
}

export default RemoveRole;