import { createReducer, createSelector, on } from "@ngrx/store";
import { UserDTO } from "../dtos/user.dto";
import { getUser, setUser } from "./user.actions";

export interface State {
    user: UserDTO;
}

const initialState: UserDTO = {} as UserDTO;

export const userReducer = createReducer(
    initialState,
    on(getUser, (state, { user }) => ({...user})),
    on(setUser, (state, { user }) => user)
);

export const userSelector = createSelector((state: State) => state.user, (user: UserDTO) => user);