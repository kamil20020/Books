import axios from "axios"
import Pageable from "../models/api/request/pageable"

class RoleService{

    private api = `${process.env.REACT_APP_API}/roles`
    
    getAll(pageable: Pageable){
        
        return axios.get(`${this.api}`, {
            params: {
                ...pageable
            }
        })
    }
}

export default new RoleService()