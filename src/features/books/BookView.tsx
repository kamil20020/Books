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

const BookView = (props: {
    book: Book,
    handleRemoveBook: (removedBookId: string) => void;
}) => {

    const book = props.book

    const [showDetails, setShowDetails] = useState<boolean>(false)

    const isUserAdmin = useAuthContext().isUserAdmin()

    return (

        <div className="book">
            <BookSimpleView book={book}/>
            <AddButton
                title={`${showDetails ? "Ukryj" : "Pokaż"} detale`}
                onClick={() => setShowDetails(!showDetails)}
            />
            {showDetails && <BookDetailedView book={book}/>}
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