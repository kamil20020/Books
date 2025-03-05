import axios from "axios"
import { Login } from "../models/api/request/login"
import { jwtDecode } from "jwt-decode"
import User from "../models/api/response/user"

class AuthService {

    private apiUrl = `${process.env.REACT_APP_API}/users`

    login(request: Login){

        return axios.post(`${this.apiUrl}/login`, request)
    }

    logout(){

        return axios.post(`${this.apiUrl}/logout`)
    }

    extractUserFromAccessToken = (data: string): User => {

        const jwtObj = jwtDecode(data)

        console.log(jwtObj)

        return {
            id: jwtObj.sub!,
            username: (jwtObj as any).username,
            roles: (jwtObj as any).roles
        }
    }

    configureAuthHeader(token: string){

        axios.defaults.headers["Authorization"] = `Bearer ${token}`
    }
}

export default new AuthService()