import { useState } from "react";
import AddButton from "../../components/AddButton";
import Img from "../../components/Img";
import Book from "../../models/api/response/book";
import BookAuthorsHeader from "./BookAuthorsHeader";
import BookSimpleView from "./BookSimpleView";
import BookDetailedView from "./BookDetailedView";
import RemoveButton from "../../components/RemoveButton";
import RemoveBook from "./RemoveBook";

const BookView = (props: {
    book: Book
}) => {

    const book = props.book

    const [showDetails, setShowDetails] = useState<boolean>(false)

    return (

        <div className="book">
            <BookSimpleView book={book}/>
            <AddButton
                title={`${showDetails ? "Ukryj" : "Pokaż"} detale`}
                onClick={() => setShowDetails(!showDetails)}
            />
            {showDetails && <BookDetailedView book={book}/>}
            <RemoveBook book={book}/>
        </div>
    )
}

export default BookView;