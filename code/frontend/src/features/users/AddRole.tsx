import { useState } from "react";
import AddButton from "../../components/AddButton"
import ConfirmationDialog from "../../components/ConfirmationDialog";
import Role from "../../models/api/response/role";
import UserRoles from "./UserRoles";
import { useRolesContext } from "../../context/RolesContext";
import UserService from "../../services/UserService";
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext";

const AddRole = (props: {
    userId: string,
    userRoles: Role[],
    addRole: (newRole: Role) => void;
}) => {

    const [selectedRoleId, setSelectedRoleId] = useState<string>()

    const setNotification = useNotificationContext().setNotification
    
    const userRolesIds = props.userRoles
        .map((role: Role) => role.id)

    const roles = useRolesContext()?.roles!

    const handleAdd = () => {

        if(!selectedRoleId){
            throw new Error("Nie wybrano roli")
        }

        UserService.createUserRole(props.userId, selectedRoleId)
        .then((response) => {

            const newRole: Role = response.data

            console.log(newRole)

            props.addRole(newRole)

            setNotification({
                message: "Przypisano rolę",
                status: NotificationStatus.SUCCESS
            })
        })
        .catch((error) => {
            console.log(error)
        })
    }

    const doesNotUserHaveRole = (role: Role): boolean => {
        
        return !userRolesIds.includes(role.id)
    }

    const handleSelectRole = (event: any) => {

        const newSelectedRole = event.target.value;

        setSelectedRoleId(newSelectedRole)
    }

    return (
        <AddButton
            showDialog
            title="Dodaj rolę"
            dialogTitle="Przypisywanie roli"
            dialogContent={
                <div className="add-role">
                    <select
                        onChange={handleSelectRole}
                    >
                        <option
                            key="empty"
                            value={undefined}
                        >
                        </option>
                        {roles
                        .filter(doesNotUserHaveRole)
                        .map((role: Role) => (
                            <option
                                key={role.id} 
                                value={role.id}
                            >
                                {role.name}
                            </option>
                        ))}
                    </select>
                </div>
            }
            onClick={handleAdd}
        />
    )
}

export default AddRole;