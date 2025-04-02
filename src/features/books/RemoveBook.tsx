import RemoveButton from "../../components/RemoveButton"
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext"
import Book from "../../models/api/response/book"
import BookService from "../../services/BookService"

const RemoveBook = (props: {
    book: Book,
    handleRemoveBook: (removedBookId: string) => void;
}) => {

    const book = props.book

    const setNotification = useNotificationContext().setNotification

    const removeBook = () => {

        BookService.deleteById(book.id)
        .then(() => {
            
            props.handleRemoveBook(book.id)

            setNotification({
                message: "Usunięto książkę",
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
            dialogTitle={`Usuwanie książki: ${book.title}`}
            onClick={removeBook}
        />
    )
}

export default RemoveBook;