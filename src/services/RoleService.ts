import axios from "axios"
import Pageable from "../models/api/request/pageable"
import CreateRole from "../models/api/request/createRole"

class RoleService{

    private api = `${process.env.REACT_APP_API}/roles`
    
    getAll(pageable: Pageable){
        
        return axios.get(`${this.api}`, {
            params: {
                ...pageable
            }
        })
    }

    create(request: CreateRole){
        
        return axios.post(this.api, request)
    }
}

export default new RoleService()