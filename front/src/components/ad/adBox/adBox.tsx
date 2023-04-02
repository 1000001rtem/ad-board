import React = require('react')
import { PaddingWrapper } from '../../paddingWrapper/paddingWrapper'
import { Grid } from '@mui/material'
import { AdCard } from '../adCard/adCard'
import { IAd } from '../../../model/ad'

interface IProps {
    ads: IAd[]
}

export const AdBox = (props: IProps) => {
    const { ads } = props
    return (
        <PaddingWrapper padding={30}>
            <Grid container>
                {ads.map((ad) => {
                    return (
                        <Grid key={ad.id} xs={4}>
                            <AdCard ad={ad} />
                        </Grid>
                    )
                })}
            </Grid>
        </PaddingWrapper>
    )
}
