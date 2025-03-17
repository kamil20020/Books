import RemoveButton from "../../components/RemoveButton";
import Author from "../../models/api/response/author";

const RemoveAuthor = (props: {
    author: Author
}) => {

    const author = props.author

    return (
        <RemoveButton
            title="Usuń"
            dialogTitle={`Usunąć autora ${author.firstname + " " + author.surname}?`}
            onClick={() => console.log("Remove author")}                    
        />
    )
}

export default RemoveAuthor;