import { useState } from "react";
import AddButton from "../../components/AddButton";
import Img from "../../components/Img";
import Book from "../../models/api/response/book";
import BookAuthorsHeader from "./BookAuthorsHeader";
import BookSimpleView from "./BookSimpleView";
import BookDetailedView from "./BookDetailedView";
import RemoveButton from "../../components/RemoveButton";
import RemoveBook from "./RemoveBook";
import { useAuthContext } from "../../context/AuthContext";
import EditBook from "./EditBook";

const BookView = (props: {
    book: Book,
    handleEdit: (updatedBook: Book) => void
    handleRemoveBook: (removedBookId: string) => void;
}) => {

    const book = props.book

    const [showDetails, setShowDetails] = useState<boolean>(false)

    const isUserLogged = useAuthContext().isUserLogged
    const isUserAdmin = useAuthContext().isUserAdmin()

    return (

        <div className="book">
            <BookSimpleView book={book}/>
            <AddButton
                title={`${showDetails ? "Ukryj" : "Pokaż"} detale`}
                onClick={() => setShowDetails(!showDetails)}
            />
            {showDetails && <BookDetailedView book={book}/>}
            {isUserLogged &&
                <EditBook book={book} handleEdit={props.handleEdit}/>
            }
            {isUserAdmin &&
                <RemoveBook
                    book={book}
                    handleRemoveBook={props.handleRemoveBook}
                />
            }
        </div>
    )
}

export default BookView;