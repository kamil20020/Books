import axios from "axios"
import Pageable from "../models/api/request/pageable"
import PatchUser from "../models/api/request/patchUser"
import CreateUser from "../models/api/request/createUser"

class UserService{

    private api = `${process.env.REACT_APP_API}/users`

    search(usernamePhrase?: string, pageable?: Pageable){

        return axios.get(this.api, {
            params: {
                ...pageable,
                username: usernamePhrase
            },
            headers: {
                Authorization: "Basic YWRtaW46YWRtaW4xMjM="
            }
        })
    }

    createUser(request: CreateUser){

        return axios.post(`${this.api}`, request, {
            headers: {
                Authorization: "Basic YWRtaW46YWRtaW4xMjM="
            }
        })
    }

    createUserRole(userId: string, roleId: string){

        return axios.post(`${this.api}/${userId}/roles/${roleId}`, {}, {
            headers: {
                Authorization: "Basic YWRtaW46YWRtaW4xMjM="
            }
        })
    }

    patchById(userId: string, newData: PatchUser){

        return axios.patch(`${this.api}/${userId}`, newData, {
            headers: {
                Authorization: "Basic YWRtaW46YWRtaW4xMjM="
            }
        })
    }

    deleteByid(userId: string){

        return axios.delete(`${this.api}/${userId}`, {
            headers: {
                Authorization: "Basic YWRtaW46YWRtaW4xMjM="
            }
        })
    }

    deleteUserRole(userId: string, roleId: string){

        return axios.delete(`${this.api}/${userId}/roles/${roleId}`, {
            headers: {
                Authorization: "Basic YWRtaW46YWRtaW4xMjM="
            }
        })
    }
}

export default new UserService()