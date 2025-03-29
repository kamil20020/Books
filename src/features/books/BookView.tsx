import Img from "../../components/Img";
import Book from "../../models/api/response/book";
import BookAuthorsHeader from "./BookAuthorsHeader";

const BookView = (props: {
    book: Book
}) => {

    const book = props.book

    return (

        <div className="book">
            <Img data={book.picture} width={200}/>
            <p>Id: {book.id}</p>
            <p>Tytuł: {book.title}</p>
            <BookAuthorsHeader authors={book.authors}/>
            <p>Data wydania: {book.publicationDate}</p>
            <p>Cena: {book.price} zł</p>
        </div>
    )
}

export default BookView;