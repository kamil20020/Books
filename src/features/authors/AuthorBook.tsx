import Img from "../../components/Img";
import Book from "../../models/api/response/book";

const AuthorBook = (props: {
    book: Book
}) => {

    const book = props.book

    return (
        <div className="author-book">
            <Img data={book.picture}/>
            <div className="author-book-details">
                <p>Tytuł: {book.title}</p>
                <p>Data publikacji: {book.publicationDate}</p>
                <p>Liczba autorów: {book.authors.length}</p>
                <p>Cena: {book.price} zł</p>
            </div>
        </div>
    )
}

export default AuthorBook;