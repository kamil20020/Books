import { useEffect, useState } from "react";
import AuthorService from "../services/AuthorService";
import Pageable from "../models/api/request/pageable";
import Author from "../models/api/response/author";
import Page from "../models/api/response/page";
import ContentHeader from "../components/ContentHeader";
import AuthorRow from "../features/authors/AuthorRow";
import "../features/authors/authors.css"
import AddButton from "../components/AddButton";
import AddAuthor from "../features/authors/AddAuthor";

const Authors = () => {

    const [authors, setAuthors] = useState<Author[]>([])

    useEffect(() => {

        const pageable: Pageable = {
            page: 0,
            size: 2
        }

        AuthorService.getPage(pageable)
        .then((response) => {

            const pagedResponse: Page<Author> = response.data

            setAuthors(pagedResponse.content)
        })
    }, [])

    return (
        <>
            <ContentHeader title="Autorzy"/>
            <div className="authors">
                <AddAuthor/>
                <table>
                    <tr className="authors-header">
                        <th>Id</th>
                        <th>Imię</th>
                        <th>Nazwisko</th>
                        <th>Główny wydawca</th>
                        <th>Liczba książek</th>
                        <th>Akcja</th>
                    </tr>
                    {authors.map((author) => (
                        <AuthorRow key={author.id} author={author}/>
                    ))}
                </table>
            </div>
        </>
    )
}

export default Authors;