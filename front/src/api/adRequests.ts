import { standardRequest } from '../utils/request/request'
import { IAd } from '../model/ad'

export const getAdByCategory = (categoryId: string) => {
    const options = {
        method: 'GET',
        url: `/api/v1/ad/find-by-category`,
        params: {
            'category-id': categoryId,
        },
    }
    return standardRequest<IAd[]>(options)
}

export const findAllActive = (limit?: number) => {
    const options = {
        method: 'GET',
        url: `/api/v1/ad/find-all-active`,
        params: {
            limit: limit,
        },
    }
    return standardRequest<IAd[]>(options)
}
