import React = require('react')
import { Grid } from '@mui/material'
import { Center, HEADER_HEIGHT, HeaderStyles } from '../../app.styled'
import { HeaderBar } from '../../components/header/headerBar'

export const Header = () => {
    return (
        <HeaderStyles minHeight={HEADER_HEIGHT}>
            <Grid container sx={{ flexGrow: 1 }}>
                <Grid xs={10} />
                <Grid xs={2}>
                    <Center>
                        <HeaderBar />
                    </Center>
                </Grid>
            </Grid>
        </HeaderStyles>
    )
}
