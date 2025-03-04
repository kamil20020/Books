import { useEffect, useRef, useState } from "react";
import UserService from "../services/UserService";
import Pageable from "../models/api/request/pageable";
import Page from "../models/api/response/page";
import User from "../models/api/response/user";
import UserRow from "../features/users/UserRow";
import ContentHeader from "../components/ContentHeader";
import RoleService from "../services/RoleService";
import Role from "../models/api/response/role";
import AddButton from "../components/AddButton";
import AddUser from "../features/users/AddUser";
import EditUser from "../features/users/EditUser";
import "../features/users/users.css"

const Users = () => {

    const [users, setUsers] = useState<User[]>([])

    let searchUsername = useRef<string>("")
    const [page, setPage] = useState<number>(0)
    let totalPages = useRef<number>(0)
    const [totalElements, setTotalElements] = useState<number>(0)
    const pageSize = 2

    useEffect(() => {

        handleSearchAndReplace()

    }, [])

    const handleSearchNextPage = () => {
        
        let searchPage = page + 1

        const pageable: Pageable = {
            page: searchPage,
            size: pageSize,
            sort: [
                
            ]
        }

        UserService.search(searchUsername.current, pageable)
        .then((response) => {

            const pagedResponse: Page<User> = response.data

            console.log(pagedResponse.content)

            setUsers([...users, ...pagedResponse.content])
            setPage(searchPage)
        })
    }

    const handleSearchAndReplace = () => {

        const pageable: Pageable = {
            page: 0,
            size: pageSize,
            sort: [
                
            ]
        }

        UserService.search(searchUsername.current, pageable)
        .then((response) => {

            const pagedResponse: Page<User> = response.data

            console.log(pagedResponse.content)

            setUsers(pagedResponse.content)
            setPage(0)
            setTotalElements(pagedResponse.totalElements)
            totalPages.current = pagedResponse.totalPages
        })
    }

    const handleAddUser = (newUser: User) => {

        setUsers([newUser, ...users])
        setTotalElements(totalElements + 1)
    }

    const handleUpdateUser = (newUser: User) => {

        const newUsers = [...users]

        const index = users
            .findIndex((user: User) => user.id == newUser.id)

        newUsers[index] = newUser

        setUsers(newUsers)
    }

    const handleRemoveUser = (toRemoveUserId: string) => {

        const newUsers = users
            .filter((user: User) => user.id !== toRemoveUserId)

        setUsers(newUsers)
        setTotalElements(totalElements - 1)
    }

    const handleChangeSearchUsername = (event: any) => {

        console.log(event.target.value)

        searchUsername.current = event.target.value
    }

    return (
        <>
            <ContentHeader title="Użytkownicy"/>
            <div className="data" style={{display: "flex", flexDirection: "column", justifyContent: "start", rowGap: 32}}>
                <div className="search-users">
                    <input 
                        type="text" 
                        placeholder="Nazwa użytkownika"
                        style={{flexGrow: 1}}
                        onChange={handleChangeSearchUsername}
                    />
                    <AddButton
                        title="Wyszukaj"
                        style={{margin: 0}}
                        onClick={handleSearchAndReplace}
                    />
                </div>
                <AddUser
                    addUser={handleAddUser}
                />
                Wyniki: {users.length} - {totalElements}
                {users.map((user: User) => (
                    <UserRow
                        key={user.id} 
                        user={user}
                        updateUser={handleUpdateUser}
                        removeUser={handleRemoveUser}
                    />
                ))}
                {page + 1 < totalPages.current &&
                    <AddButton
                        title="Załaduj więcej"
                        style={{margin: 0}}
                        onClick={handleSearchNextPage}
                    />  
                }
            </div>
        </>
    )
}

export default Users;