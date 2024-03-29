import React = require('react')
import { IAd } from '../../model/ad'
import { AdBox } from '../../components/ad/adBox/adBox'
import { useEffect, useState } from 'react'
import { findAllActive } from '../../api/adRequests'

export const Main = () => {
    const [adList, setAdList] = useState<IAd[]>([])

    useEffect(() => {
        findAllActive(6).then((result) => setAdList(result.data))
    }, [])

    return <AdBox ads={adList} />
}
