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
import SearchBooksCriteria, { SearchCriteria } from "../features/books/SearchBooksCriteria";
import { useSearchParams } from "react-router";
import BookSearchCriteria from "../models/api/request/bookSearchCriteria";

const Books = () => {

    const [searchParams, setSearchParams] = useSearchParams()

    const searchCriteria = useRef<SearchCriteria>({
        phrase: undefined,
        price: undefined,
        publicationDate: new Date()
    })

    const [books, setBooks] = useState<Book[]>([])

    const page = useRef<number>(0);
    const totalElements = useRef<number>(0);
    const pageSize = 2;

    const isUserLogged = useAuthContext().isUserLogged

    useEffect(() => {

       handleSearchFromQueryParamsAndReplace()
    }, [])

    const handleSearchAndAppend = (newPage: number) => {

        const requestSearchCriteria: BookSearchCriteria = {
            title: searchCriteria.current.phrase,
            authorName: searchCriteria.current.phrase,
            publisherName: searchCriteria.current.phrase,
            price: searchCriteria.current.price,
            publicationDate: searchCriteria.current.publicationDate
        }

        const pagination: Pageable = {
            page: newPage,
            size: pageSize
        }

        BookService.search(requestSearchCriteria, pagination)
        .then((response) => {

            const pagedResponse: Page<Book> = response.data

            setBooks([...books, ...pagedResponse.content])
            page.current = newPage
            totalElements.current = pagedResponse.totalElements
        })
    }

    const handleSearchFromQueryParamsAndReplace = () => {

        Object.keys(searchCriteria.current)
        .forEach((name: string) => {

            const value = searchParams.get(name);

            (searchCriteria.current as any)[name] = value != null ? value : undefined
        })

        const requestSearchCriteria: BookSearchCriteria = {
            title: searchCriteria.current.phrase,
            authorName: searchCriteria.current.phrase,
            publisherName: searchCriteria.current.phrase,
            price: searchCriteria.current.price,
            publicationDate: searchCriteria.current.publicationDate
        }

        const pagination: Pageable = {
            page: 0,
            size: pageSize
        }

        BookService.search(requestSearchCriteria, pagination)
        .then((response) => {

            const pagedResponse: Page<Book> = response.data

            setBooks(pagedResponse.content)
            page.current = 0
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
                <div className="search-books">
                    <SearchBooksCriteria/>
                    <AddButton
                        title="Wyszukaj"
                        style={{margin: 0, padding: "10px 18px", alignSelf: "center"}}
                        onClick={handleSearchFromQueryParamsAndReplace}
                    />
                </div>
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