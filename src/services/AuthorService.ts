import axios from "axios"
import Pageable from "../models/api/request/pageable"
import CreateAuthor from "../models/api/request/createAuthor"

class AuthorService{

    private api = `${process.env.REACT_APP_API}/authors`
    
    getPage(pageable: Pageable){

        return axios.get(this.api, {
            params: {
                pageable
            }
        })
    }

    getAuthorBooksPage(authorId: string, pageable: Pageable){

        return axios.get(`${this.api}/${authorId}/books`, {
            params: {
                pageable
            }
        })
    }

    create(request: CreateAuthor){

        return axios.post(this.api, request)
    }

    deleteById(authorId: string){

        return axios.get(`${this.api}/${authorId}`)
    }
}

export default new AuthorService()