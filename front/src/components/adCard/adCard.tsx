import React = require('react')
import { Card, Grid, Typography } from '@mui/material'
import { AdCardStyles } from './adCard.styled'
import { AdType, IAd } from '../../model/ad'
import { useNavigate } from 'react-router'
import { useDispatch } from 'react-redux'
import { setCurrentAd } from '../../store/slices/adSlice'
import { PaddingWrapper } from '../paddingWrapper/paddingWrapper'
// @ts-ignore
import defaultPic from '/public/empty.png'
import { Star } from '@mui/icons-material'

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
            {ad.type === AdType.PAID && <Star sx={{ color: 'gold', float: 'right' }} />}
            <PaddingWrapper value={20}>
                <Grid container direction={'column'}>
                    <img src={defaultPic} />
                    <Typography>{ad.theme}</Typography>
                    <br />
                    <Typography>{ad.text}</Typography>
                </Grid>
            </PaddingWrapper>
        </Card>
    )
}
