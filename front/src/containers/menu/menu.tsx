import React = require('react')
import { ListItemText, MenuItem, MenuList, Typography } from '@mui/material'
import { useEffect, useState } from 'react'
import { ICategory } from '../../model/category'
import { getAllCategories } from '../../api/categoryRequests'
import { useNavigate } from 'react-router'
import { MainLogo, MenuStyles } from './menu.styled'
// @ts-ignore
import logo from '/public/logo.png'
import { pointer } from '../../app.styled'

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
        <MenuStyles>
            <MainLogo>
                <img src={logo} style={pointer} onClick={() => navigate('/')} />
            </MainLogo>
            <Typography color={'gray'}>Категории</Typography>
            <MenuList>
                {categories.map((category) => {
                    return (
                        <MenuItem key={category.id} onClick={(event) => handleMenuClick(event, category)}>
                            <ListItemText>{category.categoryName}</ListItemText>
                        </MenuItem>
                    )
                })}
            </MenuList>
        </MenuStyles>
    )
}
