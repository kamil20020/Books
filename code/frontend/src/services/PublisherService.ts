import axios from "axios"
import Pageable from "../models/api/request/pageable"
import CreatePublisher from "../models/api/request/createPublisher"

class PublisherService{

    private api = `${process.env.REACT_APP_API}/publishers`
    
    getPublishers(pageable: Pageable){

        return axios.get(this.api, {
            params: {
                ...pageable
            }
        })
    }

    getPublisherAuthors(publisherId: string, pageable: Pageable){

        return axios.get(`${this.api}/${publisherId}/authors`, {
            params: {
                ...pageable
            }
        })
    }

    getPublisherBooks(publisherId: string, pageable: Pageable){

        return axios.get(`${this.api}/${publisherId}/books`, {
            params: {
                ...pageable
            }
        })
    }

    create(request: CreatePublisher){

        return axios.post(this.api, request);
    }

    deleteById(publisherId: string){

        return axios.delete(`${this.api}/${publisherId}`)
    }
}

export default new PublisherService()