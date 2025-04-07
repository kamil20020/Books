import Author from "../../models/api/response/author";
import { PublisherRelationshipParam } from "./PublisherRelationship";

const PublisherRelationshipRow = (props: {
    relationshipRows: PublisherRelationshipParam[]
}) => {

    return (
        <div className="publisher-relationship">
            {props.relationshipRows.map((relationshipRow: PublisherRelationshipParam) => (
                <div key={relationshipRow.name} className="publisher-relationship-row">
                    <span>{relationshipRow.name}</span>
                    <p>{relationshipRow.value}</p>
                </div>
            ))}
        </div>
    )
}

export default PublisherRelationshipRow;