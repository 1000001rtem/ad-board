import { IAd } from '../../model/ad'
import { createSlice, PayloadAction } from '@reduxjs/toolkit'

interface AdSlice {
    currentAd: IAd
}

const initialState: AdSlice = {
    currentAd: undefined,
}

export const adSlice = createSlice({
    name: 'ad',
    initialState: initialState,
    reducers: {
        setCurrentAd: (state, action: PayloadAction<IAd>) => {
            return {
                ...state,
                currentAd: action.payload,
            }
        },
    },
})

export const { setCurrentAd } = adSlice.actions
