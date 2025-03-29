import RemoveButton from "../../components/RemoveButton"
import Book from "../../models/api/response/book"

const RemoveBook = (props: {
    book: Book
}) => {

    const book = props.book

    return (
        <RemoveButton
            title="Usuń"
            dialogTitle={`Usuwanie książki: ${book.title}`}
            onClick={() => console.log("Remove book")}
        />
    )
}

export default RemoveBook;