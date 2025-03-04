import axios from "axios"
import { Login } from "../models/api/request/login"

class AuthService {

    apiUrl = `${process.env.REACT_APP_API}/users`

    login(request: Login){

        return axios.post(`${this.apiUrl}/login`, request)
    }

    logout(){

        return axios.post(`${this.apiUrl}/logout`)
    }
}

export default new AuthService()