import Keycloak from 'keycloak-js'

const _params = require('./config.json')

export interface IKeycloakParams {
    url?: string
    realm?: string
    clientId?: string
    proxy?: string
}

const params: IKeycloakParams = {
    url: _params.url || 'nodata',
    realm: _params.realm || 'nodata',
    clientId: _params.clientId || 'nodata',
}

export const isKeycloakParamsError = () => {
    const nodata = Object.keys(params).find((key) => params[key] === 'nodata')
    return nodata !== undefined
}

isKeycloakParamsError() && console.error('KeycloakParamsError', params)
const keycloak = new (Keycloak as any)(params)
export default keycloak
