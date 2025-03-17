import AddButton from "../../components/AddButton";

const AddAuthor = () => {

    return (
        <AddButton
            title="Dodaj autora"
            dialogTitle="Dodawanie autora"
            showDialog
            onClick={() => console.log("Add author")}            
        />
    )
}

export default AddAuthor;