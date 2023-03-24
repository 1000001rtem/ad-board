import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { IUser } from '../../model/user'

interface UserSlice {
    currentUser: IUser
}

const initialState: UserSlice = {
    currentUser: undefined,
}

export const userSlice = createSlice({
    name: 'user',
    initialState: initialState,
    reducers: {
        setCurrentUser: (state, action: PayloadAction<IUser>) => {
            return {
                ...state,
                currentUser: action.payload,
            }
        },
    },
})

export const { setCurrentUser } = userSlice.actions
