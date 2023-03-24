import React = require('react')
import { useSelector } from 'react-redux'
import { RootState } from '../../store/store'

export const AdContainer = () => {
    const { ad } = useSelector((state: RootState) => {
        return {
            ad: state.ad.currentAd,
        }
    })

    return <div>{ad.toString()}</div>
}
