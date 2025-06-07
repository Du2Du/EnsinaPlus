import { RoleEnum } from "../enums/roleEnum";

export interface UserDTO {
    name: string,
    email: string,
    role: RoleEnum,
    uuid: string,
    phone?: string,
    picture?: string;
}
