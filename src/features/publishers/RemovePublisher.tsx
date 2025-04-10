import RemoveButton from "../../components/RemoveButton";
import { NotificationStatus, useNotificationContext } from "../../context/NotificationContext";
import Publisher from "../../models/api/response/publisher";
import PublisherService from "../../services/PublisherService";

const RemovePublisher = (props: {
    publisher: Publisher,
    onRemove: (removedPublisherId: string) => void;
}) => {

    const publisher = props.publisher
    const setNotification = useNotificationContext().setNotification

    const handleRemovePublisher = () => {

        PublisherService.deleteById(publisher.id)
        .then((response) => {

            props.onRemove(publisher.id)

            setNotification({
                message: "Usunięto wydawcę",
                status: NotificationStatus.SUCCESS
            })
        })
        .catch((error) => {

            setNotification({
                message: error.response.data,
                status: NotificationStatus.ERROR
            })
        })
    }

    return (
        <RemoveButton
            title="Usuń wydawcę"
            dialogTitle={`Usuwanie wydawcy ${publisher.name}`}
            style={{alignSelf: "start"}}
            onClick={handleRemovePublisher}
        />
    )
}

export default RemovePublisher;