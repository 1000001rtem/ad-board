import axios, { AxiosRequestConfig } from 'axios'
import { IResponse } from '../../model/response'
import keycloak from '../../../keycloak'

export const standardNotAuthorizeRequest = <T>(options: AxiosRequestConfig): Promise<IResponse<T>> => {
    return standardRequest(options)
}

export const standardAuthorizeRequest = <T>(options: AxiosRequestConfig): Promise<IResponse<T>> => {
    options.headers = {
        Authorization: 'Bearer ' + keycloak.token,
    }
    return standardRequest(options)
}

const standardRequest = <T>(options: AxiosRequestConfig): Promise<IResponse<T>> => {
    return axios
        .request(options)
        .then((response) => {
            return {
                success: true,
                data: response.data.data,
            }
        })
        .catch((error) => {
            return {
                success: false,
                error: {
                    message: error.response.data.error.displayMessage ?? STANDARD_ERROR_MESSAGE,
                },
            }
        })
}
