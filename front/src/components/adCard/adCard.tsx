import React = require('react')
import { Card, Typography } from '@mui/material'
import { AdCardStyles } from './adCard.styled'
import { IAd } from '../../model/ad'
import { useNavigate } from 'react-router'
import { useDispatch } from 'react-redux'
import { setCurrentAd } from '../../store/slices/adSlice'

interface IProps {
    ad: IAd
}

export const AdCard = (props: IProps) => {
    const { ad } = props

    const navigate = useNavigate()
    const dispatch = useDispatch()

    const handleClick = () => {
        navigate(`/ad/${ad.id}`)
        dispatch(setCurrentAd(ad))
    }

    return (
        <Card style={AdCardStyles} onClick={handleClick}>
            <Typography>{ad.theme}</Typography>
            <Typography>{ad.text}</Typography>
        </Card>
    )
}
