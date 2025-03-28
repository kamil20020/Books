﻿import Img from "../../components/Img";
import Book from "../../models/api/response/book";
import BookAuthorsHeader from "../books/BookAuthorsHeader";

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
                <BookAuthorsHeader authors={book.authors}/>
                <p>Cena: {book.price} zł</p>
            </div>
        </div>
    )
}

export default AuthorBook;