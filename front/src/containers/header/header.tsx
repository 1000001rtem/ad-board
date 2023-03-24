import React = require('react')
import { Grid } from '@mui/material'
import { Center, HeaderStyles } from '../../app.styled'
import { HeaderBar } from '../../components/header/headerBar'

export const Header = () => {
    return (
        <HeaderStyles>
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
