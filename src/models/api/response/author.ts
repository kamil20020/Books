import Publisher from "./publisher";

export default interface Author{
    id: string,
    firstname: string,
    surname: string,
    publishedBooksCount: number,
    mainPublisher?: Publisher
}