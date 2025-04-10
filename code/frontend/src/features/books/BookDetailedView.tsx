import Book from "../../models/api/response/book";

interface DataRowProps{
    title: string,
    value?: string
}

const DataRow = (props: {
    data: DataRowProps
}) => {

    const data = props.data

    return (
        <div className="book-detailed-data-row">
            <p>{data.title}: </p>
            <p>{data.value}</p>
        </div>
    )
}

const BookDetailedView = (props: {
    book: Book
}) => {

    const book = props.book
    
    const authors = book.authors

    const data: DataRowProps[] = [
        {
            title: "Id",
            value: book.id
        },
        {
            title: "Tytuł",
            value: book.title
        },
        {
            title: "Data publikacji",
            value: book.publicationDate
        },
        {
            title: "Autorzy",
            value: authors.map(a => a.firstname + " " + a.surname).join(", ")
        },
        {
            title: "Wydawca",
            value: book.publisher.name
        },
        {
            title: "Cena",
            value: `${book.price} zł`
        }
    ]

    return (
        <div className="book-detailed">
            {data.map((data: DataRowProps) => (
                <DataRow key={data.title} data={data}/>
            ))}
        </div>
    )
}

export default BookDetailedView;