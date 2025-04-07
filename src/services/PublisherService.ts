import axios from "axios"
import Pageable from "../models/api/request/pageable"

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
}

export default new PublisherService()