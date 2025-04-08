import { useEffect, useState } from "react";
import Book from "../../models/api/response/book";
import PublisherRelationship, { PublisherRelationshipParam } from "./PublisherRelationship";
import PublisherService from "../../services/PublisherService";
import Pageable from "../../models/api/request/pageable";
import Page from "../../models/api/response/page";
import BookAuthorsHeader from "../books/BookAuthorsHeader";
import Img from "../../components/Img";

const PublisherBooks = (props: {
    publisherId: string
}) => {

    const [books, setBooks] = useState<Book[]>([])
    const pageSize = 2

    useEffect(() => {

        const pagination: Pageable = {
            page: 0,
            size: pageSize
        }

        PublisherService.getPublisherBooks(props.publisherId, pagination)
        .then((response) => {

            const pagedResponse: Page<Book> = response.data

            setBooks(pagedResponse.content)
        })
    }, [])

    const handleMapBookToRelationship = (book: Book): PublisherRelationshipParam[] => {

        return [
            {
                value: <Img data={book.picture}/>,
                isReactNode: true,
                rowStyles: {justifySelf: "center", marginBottom: 16}
            },
            {
                name: "Id",
                value: book.id
            },
            {
                name: "Tytuł",
                value: book.title
            },
            {
                name: "Wydawca",
                value: book.publisher.name
            },
            {
                name: "Data wydania",
                value: book.publicationDate
            },
            {
                name: "Cena",
                value: book.price
            },
            {
                name: "Autorzy",
                value: <BookAuthorsHeader authors={book.authors}/>,
                isReactNode: true
            }
        ]
    }

    return (

        <PublisherRelationship
            buttonPostfix={"ksiązki"}
            title={"Książki"}
            relationshipRows={books}
            relationshipMap={handleMapBookToRelationship}
        />
    )
}

export default PublisherBooks;