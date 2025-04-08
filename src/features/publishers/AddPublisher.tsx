import AddButton from "../../components/AddButton";

const AddPublisher = () => {

    return (
        <AddButton
            showDialog
            title="Dodaj wydawcę"
            dialogTitle="Dodawanie wydawcy"
            dialogContent={
                <div className="create-publisher">
                    AAA
                </div>
            }
            style={{alignSelf: "start"}}
            onClick={() => console.log("AAA")}
        />
    )
}

export default AddPublisher;