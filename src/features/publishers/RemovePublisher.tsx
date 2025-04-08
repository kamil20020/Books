import RemoveButton from "../../components/RemoveButton";
import Publisher from "../../models/api/response/publisher";

const RemovePublisher = (props: {
    publisher: Publisher
}) => {

    const publisher = props.publisher

    return (
        <RemoveButton
            title="Usuń wydawcę"
            dialogTitle={`Usuwanie wydawcy ${publisher.name}`}
            style={{alignSelf: "start"}}
            onClick={() => console.log("AA")}
        />
    )
}

export default RemovePublisher;