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
        })
    }

    createUser(request: CreateUser){

        return axios.post(`${this.api}`, request)
    }

    createUserRole(userId: string, roleId: string){

        return axios.post(`${this.api}/${userId}/roles/${roleId}`)
    }

    patchById(userId: string, newData: PatchUser){

        return axios.patch(`${this.api}/${userId}`, newData)
    }

    deleteByid(userId: string){

        return axios.delete(`${this.api}/${userId}`)
    }

    deleteUserRole(userId: string, roleId: string){

        return axios.delete(`${this.api}/${userId}/roles/${roleId}`)
    }
}

export default new UserService()