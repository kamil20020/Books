export default interface CreateBook{
    title: string;
    publicationDate: Date;
    picture?: string;
    price: number;
    publisherId: string;
    authorsIds: string[]
}