import React = require('react')
import { useParams } from 'react-router'
import { useEffect, useState } from 'react'
import { IAd } from '../../model/ad'
import { getAdByCategory } from '../../api/adRequests'
import { AdBox } from '../../components/adBox/adBox'
import { useDispatch } from 'react-redux'
import { setLoading } from '../../store/slices/appSlice'

export const CategoryContainer = () => {
    const [adList, setAdList] = useState<IAd[]>([])

    const { categoryId } = useParams()
    const dispatch = useDispatch()

    useEffect(() => {
        dispatch(setLoading(true))
        getAdByCategory(categoryId)
            .then((response) => {
                setAdList(response.data)
            })
            .finally(() => {
                dispatch(setLoading(false))
            })
    }, [categoryId])

    return <AdBox ads={adList} />
}
