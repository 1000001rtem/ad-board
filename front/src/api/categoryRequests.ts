import { standardNotAuthorizeRequest } from '../utils/request/request'
import { ICategory } from '../model/category'

export const getAllCategories = () => {
    const options = {
        method: 'GET',
        url: `/api/v1/category/all`,
    }
    return standardNotAuthorizeRequest<ICategory[]>(options)
}
