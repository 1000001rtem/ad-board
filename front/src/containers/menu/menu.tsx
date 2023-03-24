import React = require('react')
import { ListItemText, MenuItem, MenuList } from '@mui/material'
import { PaddingWrapper } from '../../components/paddingWrapper/paddingWrapper'
import { useEffect, useState } from 'react'
import { ICategory } from '../../model/category'
import { getAllCategories } from '../../api/categoryRequests'
import { useNavigate } from 'react-router'

export const Menu = () => {
    const [categories, setCategories] = useState<ICategory[]>([])

    const navigate = useNavigate()

    useEffect(() => {
        getAllCategories().then((response) => {
            if (response.success) {
                setCategories(response.data)
            }
        })
    }, [])

    const handleMenuClick = (e, category: ICategory) => {
        navigate(`/category/${category.id}`)
    }

    return (
        <PaddingWrapper>
            <MenuList>
                {categories.map((category) => {
                    return (
                        <MenuItem key={category.id} onClick={(event) => handleMenuClick(event, category)}>
                            <ListItemText>{category.categoryName}</ListItemText>
                        </MenuItem>
                    )
                })}
            </MenuList>
        </PaddingWrapper>
    )
}
