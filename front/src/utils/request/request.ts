import axios, { AxiosRequestConfig } from 'axios'
import { IResponse } from '../../model/response'

export const standardRequest = <T>(options: AxiosRequestConfig): Promise<IResponse<T>> => {
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
                    message: error.message ?? STANDARD_ERROR_MESSAGE,
                },
            }
        })
}
