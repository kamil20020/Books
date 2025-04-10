import { useState, useEffect, useRef } from "react"
import Pageable from "../../models/api/request/pageable"
import Author from "../../models/api/response/author"
import Page from "../../models/api/response/page"
import PublisherService from "../../services/PublisherService"
import PublisherRelationship, { PublisherRelationshipParam } from "./PublisherRelationship"
import PublisherRelationshipRow from "./PublisherRelationshipRow"
import { Axios, AxiosResponse } from "axios"

const PublisherAuthors = (props: {
    publisherId: string
}) => {

    const pageSize = 2

    const getSearchAndAppend = (newPage: number): Promise<AxiosResponse<any, any>> => {

        const pagination: Pageable = {
            page: newPage,
            size: pageSize
        }

        return PublisherService.getPublisherAuthors(props.publisherId, pagination)
    }

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
        <>
            <PublisherRelationship
                buttonPostfix="autorów"
                title="Autorzy"
                getSearchAndAppend={getSearchAndAppend}
                relationshipMap={relationshipMap}
            />
        </>
    )
}

export default PublisherAuthors;