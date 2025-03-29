import AddButton from "../../components/AddButton";

const AddBook = () => {

    return (
        <AddButton
            showDialog
            title="Dodaj książkę"
            dialogTitle="Dodawanie książki"
            style={{alignSelf: "start"}}
            dialogContent={
                <div className="add-book">
                    AAA
                </div>
            }
            onClick={() => console.log("Dodano książkę")}
        />
    )
}

export default AddBook;