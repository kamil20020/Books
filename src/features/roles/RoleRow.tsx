import Role from "../../models/api/response/role"
import RemoveRole from "./RemoveRole"

const RoleRow = (props: {
    role: Role,
    onRemove: (removedRoleId: string) => void
}) => {

    const role = props.role

    return (
        <div className="role-row">
            <div className="basic-role-data">
                <p>Id: {role.id}</p>
                <p>Nazwa: {role.name}</p>
            </div>
            <RemoveRole 
                role={role} 
                onRemove={props.onRemove}
            />
        </div>
    )
}

export default RoleRow;