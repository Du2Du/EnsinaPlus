import { UserDTO } from "./user.dto";

export interface CourseDTO {
  name: string,
  description: string,
  mainPicture: string,
  uuid: string,
  matriculado: boolean,
  concluido: boolean,
  owner: UserDTO
}
