import React = require('react')
import { Grid, Typography } from '@mui/material'
// @ts-ignore
import errorPic from '/public/error.png'
import { ErrorImage } from './error.styled'
import { Center } from '../../app.styled'

interface IProps {
    message?: string
}

export const Error = (props: IProps) => {
    const { message } = props
    return (
        <Center>
            <Grid container>
                <Grid item>
                    <ErrorImage src={errorPic} />
                </Grid>
                <Grid item>
                    <Typography variant={'h2'}>{message}</Typography>
                </Grid>
            </Grid>
        </Center>
    )
}
