import { useState, useEffect } from "react"
import Pageable from "../../models/api/request/pageable"
import Author from "../../models/api/response/author"
import Page from "../../models/api/response/page"
import PublisherService from "../../services/PublisherService"
import PublisherRelationship, { PublisherRelationshipParam } from "./PublisherRelationship"
import PublisherRelationshipRow from "./PublisherRelationshipRow"

const PublisherAuthors = (props: {
    publisherId: string
}) => {

    const [authors, setAuthors] = useState<Author[]>([])

    const pageSize = 2

    useEffect(() => {

        const pagination: Pageable = {
            page: 0,
            size: pageSize
        }

        PublisherService.getPublisherAuthors(props.publisherId, pagination)
        .then((response) => {

            const pagedResponse: Page<Author> = response.data

            setAuthors(pagedResponse.content)

            console.log(pagedResponse.content)
        })
    }, [])

    const relationshipMap = (author: Author): PublisherRelationshipParam[] => {

        return [
            {
                name: "Id",
                value: author.id
            },
            {
                name: "Imię i nazwisko",
                value: author.firstname + " " + author.surname
            },
            {
                name: "Liczba opublikowanych książek",
                value: author.publishedBooksCount
            }
        ]
    }

    return (
        <PublisherRelationship
            buttonPostfix="autorów"
            title="Autorzy"
            relationshipRows={authors}
            relationshipMap={relationshipMap}
        />
    )
}

export default PublisherAuthors;