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

    refreshAccessToken(refreshToken: string){

        return axios.post(`${this.apiUrl}/refresh-access-token`, {}, {
            headers: {
                Authorization: `Bearer ${refreshToken}`
            }
        })
    }

    extractUserFromAccessToken(data: string){

        const jwtObj = jwtDecode(data)

        console.log(jwtObj)

        return {
            id: jwtObj.sub!,
            username: (jwtObj as any).username,
            roles: (jwtObj as any).roles
        }
    }

    configureAuthHeader(token: string | null){

        axios.defaults.headers["Authorization"] = token ? `Bearer ${token}` : null
    }
}

export default new AuthService()