import React = require('react')
import { IAd } from '../../../model/ad'
import { Grid, TextField } from '@mui/material'
import { PaddingWrapper } from '../../paddingWrapper/paddingWrapper'
import { useSelector } from 'react-redux'
import { RootState } from '../../../store/store'
import { CategorySelector } from '../categorySelector/categorySelector'

interface IProps {
    ad: IAd
    isEdit: boolean
}

export const AdInfo = (props: IProps) => {
    const { ad, isEdit } = props

    const { categories } = useSelector((state: RootState) => {
        return {
            categories: state.category.categoryList,
        }
    })

    return (
        <PaddingWrapper top={20} right={80}>
            <Grid container direction={'column'} spacing={4}>
                <Grid item>
                    <TextField
                        id={'theme'}
                        fullWidth
                        label={'Название'}
                        defaultValue={ad.theme}
                        size={'small'}
                        inputProps={{ readOnly: !isEdit }}
                    />
                </Grid>
                <Grid item>
                    {isEdit ? (
                        <CategorySelector category={ad.categoryId} />
                    ) : (
                        <TextField
                            fullWidth
                            label={'Категория'}
                            defaultValue={categories.find((it) => it.id === ad.categoryId)?.categoryName}
                            size={'small'}
                            inputProps={{ readOnly: true }}
                        />
                    )}
                </Grid>
                <Grid item>
                    <TextField
                        id={'description'}
                        fullWidth
                        label={'Описание'}
                        defaultValue={ad.text}
                        inputProps={{ readOnly: !isEdit }}
                        multiline
                    />
                </Grid>
            </Grid>
        </PaddingWrapper>
    )
}
