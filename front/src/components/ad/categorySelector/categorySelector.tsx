import { InputLabel, MenuItem, Select } from '@mui/material'
import { useSelector } from 'react-redux'
import { RootState } from '../../../store/store'
import React = require('react')

interface IProps {
    category?: string
}

export const CategorySelector = (props: IProps) => {
    const { category } = props

    const { categories } = useSelector((state: RootState) => {
        return {
            categories: state.category.categoryList,
        }
    })

    return (
        <>
            <InputLabel id='category-select-label'>Категория</InputLabel>
            <Select inputProps={{ id: 'category' }} labelId={'category-select-label'} defaultValue={category}>
                {categories.map((category) => {
                    return (
                        <MenuItem key={category.id} value={category.id}>
                            {category.categoryName}
                        </MenuItem>
                    )
                })}
            </Select>
        </>
    )
}
