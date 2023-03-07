import React = require('react')
import { CardBoxWrapper } from './cardBox.styled'
import { Grid } from '@mui/material'

export const PaddingWrapper = ({ children }) => {
    return (
        <CardBoxWrapper>
            <Grid container>{children}</Grid>
        </CardBoxWrapper>
    )
}
