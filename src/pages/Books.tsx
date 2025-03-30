import { useEffect, useRef, useState } from "react";
import ContentHeader from "../components/ContentHeader";
import Book from "../models/api/response/book";
import BookService from "../services/BookService";
import Pageable from "../models/api/request/pageable";
import Page from "../models/api/response/page";
import BookView from "../features/books/BookView";
import "../features/books/books.css";
import AddBook from "../features/books/AddBook";

const Books = () => {

    const [books, setBooks] = useState<Book[]>([])

    const page = useRef<number>(0);
    const totalElements = useRef<number>(0);
    const pageSize = 5;

    useEffect(() => {

        const pagination: Pageable = {
            page: page.current,
            size: pageSize
        }

        BookService.getPage(pagination)
        .then((response) => {

            const pagedResponse: Page<Book> = response.data

            setBooks(pagedResponse.content)
            totalElements.current = pagedResponse.totalElements
        })
    }, [])

    return (
        <>
            <ContentHeader title="Książki"/>
            <div className="books">
                <AddBook/>
                {books.map((book) => (
                    <BookView key={book.id} book={book}/>
                ))}                
            </div>
        </>
    )
}

export default Books;