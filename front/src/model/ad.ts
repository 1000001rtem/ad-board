export interface IAd {
    id: string
    theme: string
    text: string
    type: AdType
    categoryId: string
    active: boolean
    startDate: string
    endDate?: string
}

export enum AdType {
    PAID = 'PAID',
    FREE = 'FREE',
}
