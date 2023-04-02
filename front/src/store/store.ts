import { configureStore } from '@reduxjs/toolkit'
import { adSlice } from './slices/adSlice'
import { appSlice } from './slices/appSlice'
import { userSlice } from './slices/userSlice'
import { categorySlice } from './slices/categorySlice'

export const store = configureStore({
    reducer: {
        app: appSlice.reducer,
        ad: adSlice.reducer,
        user: userSlice.reducer,
        category: categorySlice.reducer,
    },
})

export type RootState = ReturnType<typeof store.getState>
