export interface IResponse<T>{
    success: boolean
    data?: T
    error?: IWebError
}

export interface IWebError{
    message: string
}