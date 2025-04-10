import Role from "./role";

export default interface User{
    id: string,
    username: string,
    roles: Role[]
}