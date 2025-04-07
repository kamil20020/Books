import { useEffect, useState } from "react";
import Author from "../../models/api/response/author";
import PublisherService from "../../services/PublisherService";
import Pageable from "../../models/api/request/pageable";
import Page from "../../models/api/response/page";
import PublisherRelationshipRow from "./PublisherRelationshipRow";
import AddButton from "../../components/AddButton";
import ContentHeader from "../../components/ContentHeader";

export interface PublisherRelationshipParam{
    name: string,
    value: any
}

const PublisherRelationship = (props: {
    buttonPostfix: string,
    title: string,
    relationshipRows: any[],
    relationshipMap: (data: any) => PublisherRelationshipParam[]
}) => {

    const [showRelationship, setShowRelationship] = useState<boolean>(false)

    return (
        <>
            <AddButton
                title={`${showRelationship ? "Ukryj" : "Pokaż"} ${props.buttonPostfix}`}
                style={{alignSelf: "start"}}
                onClick={() => setShowRelationship(!showRelationship)}
            />
            {showRelationship &&
                <div className="publisher-relationships">
                    <ContentHeader title={props.title}/>
                    {props.relationshipRows.map((relationshioRow: any, index: number) => (
                        <PublisherRelationshipRow
                            key={index}
                            relationshipRows={props.relationshipMap(relationshioRow)}
                        />
                    ))}
                </div>
            }
        </>
    )
}

export default PublisherRelationship;