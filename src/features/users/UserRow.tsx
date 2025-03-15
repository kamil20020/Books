import AddButton from "../../components/AddButton";
import Dialog from "../../components/Dialog";
import RemoveButton from "../../components/RemoveButton";
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext";
import Role from "../../models/api/response/role";
import User from "../../models/api/response/user";
import UserService from "../../services/UserService";
import AddRole from "./AddRole";
import EditUser from "./EditUser";
import UserPersonalData from "./UserPersonalData";
import UserRoles from "./UserRoles";

const UserRow = (props: {
    user: User,
    updateUser: (user: User) => void;
    removeUser: (userId: string) => void;
}) => {
    
    const user = props.user

    const setNotification = useNotificationContext().setNotification

    const handleRemoveUser = () => {

        UserService.deleteByid(props.user.id)
        .then((response) => {

            props.removeUser(user.id)

            setNotification({
                message: "Usunięto użytkownika",
                status: NotificationStatus.SUCCESS
            })
        })
        .catch((error) => {
            console.log(error)
        })
    }

    return (
        <div key={user.id} className="user-row">
            <h3>Dane</h3>
            <UserPersonalData user={user}/>
            <h3>Role</h3>
            <UserRoles
                user={props.user}
                updateUser={props.updateUser}
            />
            <EditUser
                user={user}
                editUser={props.updateUser}
            />
            <RemoveButton
                title="Usuń użytkownika"
                dialogTitle={"Usuwanie użytkownika"}
                style={{alignSelf: "start"}}
                onClick={handleRemoveUser}
            />
        </div>
    )
}

export default UserRow;