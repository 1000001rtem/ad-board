import React = require('react')
import { useKeycloak } from '@react-keycloak/web'
import { AuthenticatedHeaderBar } from './authenticatedHeaderBar'
import { NotAuthenticatedHeaderBar } from './notAuthenticatedHeaderBar'

export const HeaderBar = () => {
    const { keycloak } = useKeycloak()

    return <>{keycloak.authenticated ? <AuthenticatedHeaderBar /> : <NotAuthenticatedHeaderBar />}</>
}
