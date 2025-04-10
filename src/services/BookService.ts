import axios from "axios";
import Pageable from "../models/api/request/pageable";
import CreateBook from "../models/api/request/createBook";
import BookSearchCriteria from "../models/api/request/bookSearchCriteria";
import PatchBook from "../models/api/request/patchBook";

class BookService {

    private api = `${process.env.REACT_APP_API}/books`;

    getPage(pagination: Pageable){

        return axios.get(this.api, {
            params: {
                ...pagination
            }
        })
    }

    search(searchCriteria: BookSearchCriteria, pagination: Pageable){

        let publicationDate: string | undefined = undefined

        if(searchCriteria.publicationDate !== undefined){
            publicationDate = new Date(searchCriteria.publicationDate)?.toISOString().slice(0, 10)
        }
        
        return axios.get(`${this.api}/search`, {
            params: {
                ...searchCriteria,
                publicationDate: publicationDate,
                ...pagination
            }
        })
    }

    create(request: CreateBook){

        return axios.post(this.api, request)
    }

    patchById(bookId: string, request: PatchBook){

        return axios.patch(`${this.api}/${bookId}`, request)
    }

    deleteById(bookId: string){

        return axios.delete(`${this.api}/${bookId}`)
    }
}

export default new BookService();
