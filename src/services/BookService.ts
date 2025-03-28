import axios from "axios";
import Pageable from "../models/api/request/pageable";
import CreateBook from "../models/api/request/createBook";

class BookService {

    private api = `${process.env.REACT_APP_API}/books`;

    getPage(pagination: Pageable){

        return axios.get(this.api, {
            params: {
                ...pagination
            }
        })
    }

    create(request: CreateBook){

        return axios.post(this.api, request)
    }

    deleteById(bookId: string){

        return axios.delete(`${this.api}/${bookId}`)
    }
}

export default new BookService();
