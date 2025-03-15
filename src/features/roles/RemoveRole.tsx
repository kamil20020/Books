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

        setNotification({
            message: "Usunięto rolę",
            status: NotificationStatus.SUCCESS
        })

        props.onRemove(role.id)
    }

    return (
        <RemoveButton
            title="Usuń rolę"
            dialogTitle={`Czy usunąć rolę: ${role.name}?`} 
            onClick={handleRemove}  
        />
    )
}

export default RemoveRole;