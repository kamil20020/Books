import { useEffect, useRef, useState } from "react";
import ContentHeader from "../components/ContentHeader";
import Role from "../models/api/response/role";
import "../features/roles/roles.css"
import RoleService from "../services/RoleService";
import Pageable from "../models/api/request/pageable";
import Page from "../models/api/response/page";
import RoleRow from "../features/roles/RoleRow";
import AddButton from "../components/AddButton";
import AddRole from "../features/roles/AddRole";

const Roles = () => {

    const [roles, setRoles] = useState<Role[]>([])
    const pageNumber = useRef<number>(0)
    const totalElements = useRef<number>(0)
    const pageSize = 10

    useEffect(() => {

        handleSearchAndAppend(0)
    }, [])

    const handleSearchAndAppend = (page: number) => {

        const pageable: Pageable = {
            page: page,
            size: pageSize
        }

        RoleService.getAll(pageable)
        .then((response) => {

            const newRolesPage: Page<Role> = response.data

            pageNumber.current = newRolesPage.number
            totalElements.current = page
            setRoles([...roles, ...newRolesPage.content])

            console.log(roles)
        })
    }

    const handleAdd = (newRole: Role) => {

        setRoles([newRole, ...roles])
    }

    const handleRemove = (removedRoleId: string) => {

        const newRoles = roles
            .filter((role: Role) => role.id !== removedRoleId)

        setRoles(newRoles)
    }

    return (
        <>
            <ContentHeader title="Role"/>
            <div className="roles">
                <AddRole
                    onAdd={handleAdd}
                />
                {roles.map((role: Role) => (
                    <RoleRow 
                        key={role.id} 
                        role={role} 
                        onRemove={handleRemove}
                    />
                ))}
            </div>
        </>
    )
}

export default Roles;