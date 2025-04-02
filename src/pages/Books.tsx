import { useEffect, useRef, useState } from "react";
import ContentHeader from "../components/ContentHeader";
import Book from "../models/api/response/book";
import BookService from "../services/BookService";
import Pageable from "../models/api/request/pageable";
import Page from "../models/api/response/page";
import BookView from "../features/books/BookView";
import "../features/books/books.css";
import AddBook from "../features/books/AddBook";
import AddButton from "../components/AddButton";
import { useAuthContext } from "../context/AuthContext";

const Books = () => {

    const [books, setBooks] = useState<Book[]>([])

    const page = useRef<number>(0);
    const totalElements = useRef<number>(0);
    const pageSize = 2;

    const isUserLogged = useAuthContext().isUserLogged

    useEffect(() => {

        handleSearchAndAppend(0)
    }, [])

    const handleSearchAndAppend = (newPage: number) => {

        const pagination: Pageable = {
            page: newPage,
            size: pageSize
        }

        BookService.getPage(pagination)
        .then((response) => {

            const pagedResponse: Page<Book> = response.data

            setBooks([...books, ...pagedResponse.content])
            page.current = newPage
            totalElements.current = pagedResponse.totalElements
        })
    }

    const handleAddBook = (newBook: Book) => {

        setBooks([newBook, ...books])
        totalElements.current = totalElements.current + 1
    }

    const handleRemoveBook = (deletedBookId: string) => {

        const newBooks = books
            .filter((book: Book) => book.id !== deletedBookId)

        setBooks(newBooks)
        totalElements.current = totalElements.current - 1
    }

    const Pagination = () => (
        
        <>
            {books.length < totalElements.current &&
                <AddButton
                    title="Załaduj dane"
                    onClick={() => handleSearchAndAppend(page.current + 1)}
                />
            }
        </>
    )

    return (
        <>
            <ContentHeader title="Książki"/>
            <div className="books">
                {isUserLogged &&
                    <AddBook handleAddBook={handleAddBook}/>
                }
                {books.map((book) => (
                    <BookView
                        key={book.id}
                        book={book}
                        handleRemoveBook={handleRemoveBook}
                    />
                ))}
                <Pagination/>
            </div>
        </>
    )
}

export default Books;