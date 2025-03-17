import Icon from "../../components/Icon";
import RemoveButton from "../../components/RemoveButton";
import Author from "../../models/api/response/author";
import AuthorBooks from "./AuthorBooks";
import RemoveAuthor from "./RemoveAuthor";

const AuthorRow = (props: {
    author: Author
}) => {

    const author = props.author

    return (
        <tr className="author-row">
            <td>{author.id}</td>
            <td>{author.firstname}</td>
            <td>{author.surname}</td>
            <td>{author.mainPublisherId ? author.mainPublisherId : "Brak"}</td>
            <td>
               <AuthorBooks author={author}/>
            </td>
            <td className="author-actions">
                <RemoveAuthor author={author}/>
            </td>
        </tr>
    )
}

export default AuthorRow;