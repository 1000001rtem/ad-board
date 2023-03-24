import React = require('react')
import { useKeycloak } from '@react-keycloak/web'
import { Login } from '@mui/icons-material'
import { pointer } from '../../app.styled'

export const NotAuthenticatedHeaderBar = () => {
    const { keycloak } = useKeycloak()

    const login = () => {
        keycloak.login()
    }

    return <Login style={pointer} onClick={login} />
}
