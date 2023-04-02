import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { ICategory } from '../../model/category'

interface CategorySlice {
    categoryList: ICategory[]
}

const initialState: CategorySlice = {
    categoryList: [],
}

export const categorySlice = createSlice({
    name: 'category',
    initialState: initialState,
    reducers: {
        setCategoryList: (state, action: PayloadAction<ICategory[]>) => {
            return {
                ...state,
                categoryList: action.payload,
            }
        },
    },
})

export const { setCategoryList } = categorySlice.actions
