import { useEffect, useRef, useState } from "react";
import Author from "../../models/api/response/author";
import PublisherService from "../../services/PublisherService";
import Pageable from "../../models/api/request/pageable";
import Page from "../../models/api/response/page";
import PublisherRelationshipRow from "./PublisherRelationshipRow";
import AddButton from "../../components/AddButton";
import ContentHeader from "../../components/ContentHeader";
import { AxiosResponse } from "axios";

export interface PublisherRelationshipParam{
    name?: string,
    value: any,
    isReactNode?: boolean,
    rowStyles?: React.CSSProperties
}

const PublisherRelationship = (props: {
    buttonPostfix: string,
    title: string,
    getSearchAndAppend: (newPage: number) => Promise<AxiosResponse<any, any>>;
    relationshipMap: (data: any) => PublisherRelationshipParam[]
}) => {

    const [showRelationship, setShowRelationship] = useState<boolean>(false)

    const [relationshipRows, setRelationshipRows] = useState<PublisherRelationshipParam[][]>([])

    const page = useRef<number>(0)
    const totalElements = useRef<number>(0)

    const handleSearchAndAppend = (newPage: number) => {

        props.getSearchAndAppend(newPage)
        .then((response) => {
        
            const pagedResponse: Page<Author> = response.data

            const mappedContent: PublisherRelationshipParam[][] = pagedResponse.content
                .map((data: any) => props.relationshipMap(data))

            setRelationshipRows([...relationshipRows, ...mappedContent])
            page.current = newPage
            totalElements.current = pagedResponse.totalElements
        })
    }

    useEffect(() => {

        handleSearchAndAppend(0)
    }, [])

    return (
        <>
            <AddButton
                title={`${showRelationship ? "Ukryj" : "Pokaż"} ${props.buttonPostfix}`}
                style={{alignSelf: "start"}}
                onClick={() => setShowRelationship(!showRelationship)}
            />
            {showRelationship &&
                <div className="publisher-relationships">
                    <ContentHeader title={relationshipRows.length > 0 ? "Autorzy" : "Brak"}/>
                    {relationshipRows.length > 0 &&
                        relationshipRows.map((relationshipRow: PublisherRelationshipParam[]) => (
                            <PublisherRelationshipRow
                                key={props.title}
                                relationshipRows={relationshipRow}
                            />
                        ))
                    }
                    {relationshipRows.length < totalElements.current &&
                        <AddButton
                            title="Dodładuj dane"
                            onClick={() => handleSearchAndAppend(page.current + 1)}
                        />
                    }
                </div>
            }
        </>
    )
}

export default PublisherRelationship;