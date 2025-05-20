import { createAction } from "@ngrx/store";
import { UserDTO } from "../dtos/user.dto";

export const getUser = createAction('[User] Get User', (user: UserDTO) => ({ user }));

export const setUser = createAction('[User] Set User', (user: UserDTO) => ({ user }));