import { useEffect, useRef, useState } from "react";
import AuthorService from "../services/AuthorService";
import Pageable from "../models/api/request/pageable";
import Author from "../models/api/response/author";
import Page from "../models/api/response/page";
import ContentHeader from "../components/ContentHeader";
import AuthorRow from "../features/authors/AuthorRow";
import "../features/authors/authors.css"
import AddButton from "../components/AddButton";
import AddAuthor from "../features/authors/AddAuthor";
import Dropdown from "../components/Dropdown";
import PageSizeDropdown from "../components/PageSizeDropdown";
import Icon from "../components/Icon";
import { unstable_batchedUpdates } from "react-dom";

interface Pagination{
    page: number,
    totalElements: number
    pageSize: number,
}

const TableHeader = () => (

    <thead>
        <tr className="authors-header">
            <th>Id</th>
            <th>Imię</th>
            <th>Nazwisko</th>
            <th>Główny wydawca</th>
            <th>Liczba książek</th>
            <th>Akcja</th>
        </tr>
    </thead>
)

const Authors = () => {

    const [authors, setAuthors] = useState<Author[]>([])
    const [pagination, setPagination] = useState<Pagination>({
        page: 0,
        totalElements: 0,
        pageSize: 1
    })

    useEffect(() => {

        handleSearch(0)
    }, [pagination.pageSize])

    const handleSearch = (newPage: number) => {

        const pageable: Pageable = {
            page: newPage,
            size: pagination.pageSize
        }

        AuthorService.getPage(pageable)
        .then((response) => {

            const pagedResponse: Page<Author> = response.data

            setAuthors(pagedResponse.content)
            setPagination({
                ...pagination,
                page: newPage,
                totalElements: pagedResponse.totalElements
            })
        })
    }

    const handleAdd = (newAuthor: Author) => {

        unstable_batchedUpdates(() => {
            setPagination({
                ...pagination, 
                page: 0,
                totalElements: pagination.totalElements + 1
            })
            setAuthors([newAuthor, ...authors])
        })
    }

    const handleDelete = (deletedAuthorId: string) => {

        const newAuthors = authors
            .filter((author: Author) => author.id !== deletedAuthorId)
        
        unstable_batchedUpdates(() => {
            setAuthors(newAuthors)
            setPagination({
                ...pagination,
                totalElements: pagination.totalElements - 1
            })
        })
    }

    return (
        <>
            <ContentHeader title="Autorzy"/>
            <div className="authors">
                <AddAuthor handleAdd={handleAdd}/>
                <table>
                    <TableHeader/>
                    <tbody>
                        {authors.map((author) => (
                            <AuthorRow
                                key={author.id}
                                author={author}
                                onDeleteAuthor={handleDelete}
                            />
                        ))}
                    </tbody>
                </table>
                <div className="pagination">
                    <div className="pagination-size-select">
                        Wierszy na stronie:
                        <PageSizeDropdown
                            defaultValue={1}
                            onSelect={(newPageSize: string) => {
                                setPagination({...pagination, pageSize: +newPageSize})
                            }}
                        />
                    </div>
                    {(pagination.page * pagination.pageSize) + authors.length} - {pagination.totalElements}
                    <div className="change-page">
                        <Icon
                            iconName="arrow_back_ios"
                            isDisabled={pagination.page == 0}
                            onClick={() => handleSearch(pagination.page - 1)}
                        />
                        <Icon
                            iconName="arrow_forward_ios"
                            isDisabled={(pagination.page + 1) * pagination.pageSize >= pagination.totalElements}
                            onClick={() => handleSearch(pagination.page + 1)}
                        />
                    </div>
                </div>
            </div>
        </>
    )
}

export default Authors;