import { standardAuthorizeRequest, standardNotAuthorizeRequest } from '../utils/request/request'
import { IAd } from '../model/ad'

export const getAdByCategory = (categoryId: string) => {
    const options = {
        method: 'GET',
        url: `/api/v1/ad/find-by-category`,
        params: {
            'category-id': categoryId,
        },
    }
    return standardNotAuthorizeRequest<IAd[]>(options)
}

export const findAllActive = (limit?: number) => {
    const options = {
        method: 'GET',
        url: `/api/v1/ad/find-all-active`,
        params: {
            limit: limit,
        },
    }
    return standardNotAuthorizeRequest<IAd[]>(options)
}

export const findById = (id: string) => {
    const options = {
        method: 'GET',
        url: `/api/v1/ad/find-by-id`,
        params: {
            id,
        },
    }
    return standardNotAuthorizeRequest<IAd>(options)
}

export const createAd = (request) => {
    const options = {
        method: 'POST',
        url: `/api/v1/ad/create`,
        data: request,
    }
    return standardAuthorizeRequest<string>(options)
}

export const updateAd = (request) => {
    const options = {
        method: 'PUT',
        url: `/api/v1/ad/update`,
        data: request,
    }
    return standardAuthorizeRequest<string>(options)
}
