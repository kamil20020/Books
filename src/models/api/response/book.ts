import Author from "./author";

export default interface Book{
    id: string;
    title: string
    price: number,
    picture?: string
    publicationDate: string,
    publisher: any,
    authors: Author[]
}