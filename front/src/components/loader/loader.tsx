import React = require('react')
import { CircularProgress } from '@mui/material'
import { LoaderWrapper } from './loader.styled'

export const Loader = () => {
    return (
        <LoaderWrapper>
            <CircularProgress color={'inherit'} />
        </LoaderWrapper>
    )
}
