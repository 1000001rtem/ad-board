import React = require('react')
import { Grid, Typography } from '@mui/material'
import { AccountCircleRounded, Notifications } from '@mui/icons-material'
import { useDispatch, useSelector } from 'react-redux'
import { RootState } from '../../store/store'
import { useKeycloak } from '@react-keycloak/web'
import { useEffect } from 'react'
import { setCurrentUser } from '../../store/slices/userSlice'
import { pointer } from '../../app.styled'

export const AuthenticatedHeaderBar = () => {
    const dispatch = useDispatch()
    const { keycloak } = useKeycloak()
    const { user } = useSelector((state: RootState) => {
        return {
            user: state.user.currentUser,
        }
    })

    useEffect(() => {
        const user = {
            name: keycloak.tokenParsed.given_name,
            surname: keycloak.tokenParsed.family_name,
            email: keycloak.tokenParsed.email,
        }
        dispatch(setCurrentUser(user))
    }, [])

    return (
        <Grid container direction={'row'} justifyContent={'center'} spacing={4}>
            <Grid item>
                <Notifications />
            </Grid>
            <Grid item>
                <Grid container spacing={1}>
                    <Grid item>
                        <Typography variant={'body1'}>{`${user?.surname} ${user?.name}`}</Typography>
                    </Grid>
                    <Grid item>
                        <AccountCircleRounded style={pointer} onClick={() => keycloak.logout()} />
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    )
}
