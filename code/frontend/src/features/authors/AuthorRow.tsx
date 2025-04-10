import Icon from "../../components/Icon";
import RemoveButton from "../../components/RemoveButton";
import { useAuthContext } from "../../context/AuthContext";
import Author from "../../models/api/response/author";
import AuthorBooks from "./AuthorBooks";
import RemoveAuthor from "./RemoveAuthor";

const AuthorRow = (props: {
    author: Author,
    onDeleteAuthor: (deletedAuthorId: string) => void
}) => {

    const author = props.author
    const isUserAdmin = useAuthContext().isUserAdmin()

    return (
        <tr className="author-row">
            <td>{author.id}</td>
            <td>{author.firstname}</td>
            <td>{author.surname}</td>
            <td>{author.mainPublisher ? author.mainPublisher.name : "Brak"}</td>
            <td>
               <AuthorBooks author={author}/>
            </td>
            <td className="author-actions">
                {isUserAdmin &&
                    <RemoveAuthor
                        author={author}
                        onDeleteAuthor={props.onDeleteAuthor}
                    />
                }
            </td>
        </tr>
    )
}

export default AuthorRow;