import RemoveButton from "../../components/RemoveButton";
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext";
import Author from "../../models/api/response/author";
import AuthorService from "../../services/AuthorService";

const RemoveAuthor = (props: {
    author: Author,
    onDeleteAuthor: (deletedAuthorId: string) => void
}) => {

    const author = props.author

    const setNotification = useNotificationContext().setNotification

    const handleDeleteAuthor = () => {

        AuthorService.deleteById(author.id)
        .then((response) => {

            props.onDeleteAuthor(author.id)

            setNotification({
                message: "Usunięto autora",
                status: NotificationStatus.SUCCESS
            })
        })
        .catch((error) => {

            console.log(error)

            setNotification({
                message: error.response.data,
                status: NotificationStatus.ERROR
            })
        })
    }

    return (
        <RemoveButton
            title="Usuń"
            dialogTitle={`Usunąć autora ${author.firstname + " " + author.surname}?`}
            onClick={handleDeleteAuthor}                    
        />
    )
}

export default RemoveAuthor;