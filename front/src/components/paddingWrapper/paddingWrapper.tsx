import React = require('react')
import { PaddingWrapperBox } from './paddingWrapper.styled'
import { Grid } from '@mui/material'

interface IProps {
    value?: number
    children: any
}

export const PaddingWrapper = (props: IProps) => {
    const { value, children } = props
    return (
        <PaddingWrapperBox value={value}>
            <Grid container>{children}</Grid>
        </PaddingWrapperBox>
    )
}
