import RemoveButton from "../../components/RemoveButton";
import User from "../../models/api/response/user";

const UserPersonalData = (props: {
    user: User
}) => {

    const user = props.user

    return (
        <div>
            <p>Id: {user.id} </p>
            <p>Nazwa użytkownika: {user.username}</p>
        </div>
    )
}

export default UserPersonalData;