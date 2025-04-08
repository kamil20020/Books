import Author from "../../models/api/response/author";
import { PublisherRelationshipParam } from "./PublisherRelationship";

const PublisherRelationshipRow = (props: {
    relationshipRows: PublisherRelationshipParam[]
}) => {

    return (
        <div className="publisher-relationship">
            <hr style={{border: "1px solid black", height: 1, width: "100%", marginBottom: 22}}/>
            <div className="publisher-relationship-rows">
                {props.relationshipRows.map((relationshipRow: PublisherRelationshipParam) => (
                    <div
                        key={relationshipRow.name}
                        className="publisher-relationship-row"
                        style={{...relationshipRow.rowStyles}}
                    >
                        {relationshipRow.name && <span>{relationshipRow.name}:</span>}
                        {!relationshipRow.isReactNode ? 
                                <p>{relationshipRow.value}</p> 
                            : 
                                relationshipRow.value
                        }
                    </div>
                ))}
            </div>
        </div>
    )
}

export default PublisherRelationshipRow;