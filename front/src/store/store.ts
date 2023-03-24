import { configureStore } from '@reduxjs/toolkit'
import { adSlice } from './slices/adSlice'
import { appSlice } from './slices/appSlice'

export const store = configureStore({
    reducer: {
        app: appSlice.reducer,
        ad: adSlice.reducer,
    },
})

export type RootState = ReturnType<typeof store.getState>
