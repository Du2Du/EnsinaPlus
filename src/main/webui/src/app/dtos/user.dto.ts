import { RoleEnum } from "../enums/roleEnum";
import { UserTypeEnum } from "../enums/userTypeEnum";

export interface UserDTO {
    name: string,
    email: string,
    role: RoleEnum,
    type: UserTypeEnum,
    uuid: string,
    phone?: string,
    picture?: string;
}
