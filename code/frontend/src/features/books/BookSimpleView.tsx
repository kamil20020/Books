import Img from "../../components/Img"
import Book from "../../models/api/response/book"
import BookAuthorsHeader from "./BookAuthorsHeader"

const BookSimpleView = (props: {
    book: Book
}) => {

    const book = props.book
    
    return (
        <div className="book-simple">
            <Img data={book.picture} width={200}/>
            <div className="book-simple-details">
                <div className="book-simple-details-basic">
                    <h3>{book.title}</h3>
                    <BookAuthorsHeader authors={book.authors}/>
                    <p>Data wydania: {book.publicationDate}</p>
                </div>
                <h2>{book.price} zł</h2>
            </div>
        </div>
    )
}

export default BookSimpleView;