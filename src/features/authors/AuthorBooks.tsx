import { useEffect, useState } from "react";
import Dialog from "../../components/Dialog";
import Icon from "../../components/Icon";
import Author from "../../models/api/response/author";
import Book from "../../models/api/response/book";
import AuthService from "../../services/AuthService";
import AuthorService from "../../services/AuthorService";
import Pageable from "../../models/api/request/pageable";
import Page from "../../models/api/response/page";

const AuthorBooks = (props: {
    author: Author
}) => {

    const author = props.author

    const [books, setBooks] = useState<Book[]>([])

    const [showBooks, setShowBooks] = useState<boolean>(false)

    useEffect(() => {

        const pageable: Pageable = {
            page: 0,
            size: 5
        }

        AuthorService.getAuthorBooksPage(author.id, pageable)
        .then((response) => {

            const pagedResponse: Page<Book> = response.data

            setBooks(pagedResponse.content)
        })
    }, [])

    return (
        <div className="author-books-info">
            <p>{author.publishedBooksCount}</p>
            <Icon 
                iconName="menu_book" 
                size={28} 
                onClick={() => setShowBooks(true)}
            />
            <Dialog
                title={`Książki autorstwa ${author.firstname} ${author.surname}`} 
                isOpened={showBooks} 
                content={
                    <div className="author-books">
                        {books.map((book) => (
                            <div>
                                {book.id}
                                {book.title}
                            </div>
                        ))}
                    </div>
                }
                onClose={() => setShowBooks(false)}              
            />
        </div>
    )
}

export default AuthorBooks;