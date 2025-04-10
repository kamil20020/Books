import { useEffect, useState } from "react";
import Book from "../../models/api/response/book";
import PublisherRelationship, { PublisherRelationshipParam } from "./PublisherRelationship";
import PublisherService from "../../services/PublisherService";
import Pageable from "../../models/api/request/pageable";
import Page from "../../models/api/response/page";
import BookAuthorsHeader from "../books/BookAuthorsHeader";
import Img from "../../components/Img";
import { AxiosResponse } from "axios";

const PublisherBooks = (props: {
    publisherId: string
}) => {

    const pageSize = 3

    const getSearchAndAppend = (page: number): Promise<AxiosResponse<any, any>> => {

        const pagination: Pageable = {
            page: page,
            size: pageSize
        }

        return PublisherService.getPublisherBooks(props.publisherId, pagination)
    }

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
            getSearchAndAppend={getSearchAndAppend}
            relationshipMap={handleMapBookToRelationship}
        />
    )
}

export default PublisherBooks;