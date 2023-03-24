import { createSlice, PayloadAction } from '@reduxjs/toolkit'

interface AppSlice {
    loading: boolean
}

const initialState: AppSlice = {
    loading: false,
}

export const appSlice = createSlice({
    name: 'app',
    initialState: initialState,
    reducers: {
        setLoading: (state, action: PayloadAction<boolean>) => {
            return {
                ...state,
                loading: action.payload,
            }
        },
    },
})

export const { setLoading } = appSlice.actions
