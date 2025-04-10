import Author from "./author";
import Publisher from "./publisher";

export default interface Book{
    id: string;
    title: string
    price: number,
    picture?: string
    publicationDate: string,
    publisher: Publisher,
    authors: Author[]
}