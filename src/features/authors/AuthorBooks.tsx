import { useEffect, useRef, useState } from "react";
import Dialog from "../../components/Dialog";
import Icon from "../../components/Icon";
import Author from "../../models/api/response/author";
import Book from "../../models/api/response/book";
import AuthService from "../../services/AuthService";
import AuthorService from "../../services/AuthorService";
import Pageable from "../../models/api/request/pageable";
import Page from "../../models/api/response/page";
import AuthorBook from "./AuthorBook";
import AddButton from "../../components/AddButton";

const AuthorBooks = (props: {
    author: Author
}) => {

    const author = props.author

    const [books, setBooks] = useState<Book[]>([])

    const page = useRef<number>(0);
    const totalElements = useRef<number>(0);
    const pageSize = 3;

    const [showBooks, setShowBooks] = useState<boolean>(false)

    useEffect(() => {

        searchAndAppend(0)
    }, [])

    const searchAndAppend = (newPage: number) => {

        const pageable: Pageable = {
            page: newPage,
            size: pageSize
        }

        AuthorService.getAuthorBooksPage(author.id, pageable)
        .then((response) => {

            const pagedResponse: Page<Book> = response.data

            setBooks([...books, ...pagedResponse.content])
            page.current = newPage
            totalElements.current = pagedResponse.totalElements
        
        })
    }

    return (
        <div className="author-books-info">
            <p>{author.publishedBooksCount}</p>
            {author.publishedBooksCount > 0 &&
                 <Icon 
                    iconName="menu_book" 
                    size={28} 
                    onClick={() => setShowBooks(true)}
                />
            }
            <Dialog
                title={`Książki autorstwa ${author.firstname} ${author.surname}`} 
                isOpened={showBooks}
                content={
                    <div className="author-books">
                        {books.map((book: Book) => (
                            <AuthorBook key={book.id} book={book}/>
                        ))}
                        {books.length < totalElements.current &&
                            <AddButton
                                title="Załaduj"
                                onClick={() => searchAndAppend(page.current + 1)}
                            />
                        }
                    </div>
                }
                onClose={() => setShowBooks(false)}              
            />
        </div>
    )
}

export default AuthorBooks;