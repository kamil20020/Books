import Author from "../../models/api/response/author"

const BookAuthorsHeader = (props: {
    authors: Author[]
}) => {

    const authors = props.authors

    return (

        <p 
            title={
                authors
                    .map(a => a.firstname + " " + a.surname)
                    .join(", ")}
        >
            Autorzy: {authors[0].firstname + " " + authors[0].surname} {authors.length > 1 && " (...)"}
        </p>
    )
}

export default BookAuthorsHeader;