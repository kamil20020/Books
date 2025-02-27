﻿import { createContext, SetStateAction, useContext, useEffect, useState } from "react"
import Role from "../models/api/response/role"
import RoleService from "../services/RoleService";
import Pageable from "../models/api/request/pageable";
import Page from "../models/api/response/page";

interface RolesConextType{
    roles: Role[],
    // setRoles: React.Dispatch<SetStateAction<Role[]>>;
}

const RolesContext = createContext<RolesConextType | undefined>(undefined)

export const RolesProvider = (props: {
    content: React.ReactNode
}) => {

    const [roles, setRoles] = useState<Role[]>([])

    useEffect(() => {

        const pageable: Pageable = {
            page: 0,
            size: 10,
            sort: [
                
            ]
        }

        RoleService.getAll(pageable)
        .then((response) => {

            const pagedResponse: Page<Role> = response.data

            console.log(pagedResponse.content)

            setRoles(pagedResponse.content)
        })
    }, [])

    return (
        <RolesContext.Provider value={{roles}}>
            {props.content}
        </RolesContext.Provider>
    )
}

export const useRolesContext = () => {

    const context = useContext(RolesContext);

    if(!context){
        throw new Error("roles context value is undefined")
    }

    return useContext(RolesContext);
}