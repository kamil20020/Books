import Book from "../../models/api/response/book";
import PublisherRelationship, { PublisherRelationshipParam } from "./PublisherRelationship";

const PublisherBooks = () => {

    return (

        <PublisherRelationship
            buttonPostfix={"ksiązki"}
            title={"Książki"}
            relationshipRows={[]}
            relationshipMap={(book: Book) => [{name: "AA", value: "DD"}]}
        />
    )
}

export default PublisherBooks;