import RemoveButton from "../../components/RemoveButton";
import Publisher from "../../models/api/response/publisher";
import PublisherAuthors from "./PublisherAuthors";
import PublisherBooks from "./PublisherBooks";
import RemovePublisher from "./RemovePublisher";

const PublisherView = (props: {
    publisher: Publisher
}) => {

    const publisher = props.publisher

    return (
        <div className="publisher">
            <div className="publisher-details">
                <div className="publisher-row">
                    <span>Id:</span>
                    <p>{publisher.id}</p>
                </div>
                <div className="publisher-row">
                    <span>Nazwa:</span>
                    <p>{publisher.name}</p>
                </div>
            </div>
            <PublisherAuthors publisherId={publisher.id}/>
            <PublisherBooks publisherId={publisher.id}/>
            <RemovePublisher publisher={publisher}/>
        </div>
    )
}

export default PublisherView;