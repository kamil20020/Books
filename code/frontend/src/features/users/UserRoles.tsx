import AddButton from "../../components/AddButton"
import RemoveButton from "../../components/RemoveButton";
import Role from "../../models/api/response/role";
import User from "../../models/api/response/user";
import AddRole from "./AddRole";
import RemoveRole from "./RemoveRole";

const UserRoles = (props: {
    user: User,
    updateUser: (user: User) => void;
}) => {

    const user = props.user
    const userId = user.id
    const userRoles = user.roles

    const handleAddRole = (newRole: Role) => {

        const newUser: User = JSON.parse(JSON.stringify(user))

        console.log(newRole)
        
        newUser.roles = [newRole, ...newUser.roles]

        props.updateUser(newUser)
    }

    const handleRemoveRole = (roleId: string) => {

        const newUser: User = JSON.parse(JSON.stringify(user))
        
        newUser.roles = newUser.roles
            .filter((role: Role) => role.id != roleId)

        props.updateUser(newUser)
    }

    return (
        <div className="user-roles">
            {userRoles ?
            
                userRoles.map((role: Role) => (
                    <div key={role.id}>
                        {role.name}
                        <RemoveRole 
                            userId={userId} 
                            roleId={role.id}
                            removeRole={handleRemoveRole}
                        />
                    </div>
                ))

                :
                
                <p>Brak ról</p>
            }
            <AddRole
                userId={userId}
                userRoles={userRoles}
                addRole={handleAddRole}
            />
        </div>
    )
}

export default UserRoles;