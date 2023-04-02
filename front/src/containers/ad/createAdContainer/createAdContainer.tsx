import { Button, Grid } from '@mui/material'
import { AdInfo } from '../../../components/ad/adInfo/adInfo'
import { createAd } from '../../../api/adRequests'
import { useState } from 'react'
import { IError } from '../../../model/common'
import { useNavigate } from 'react-router'
import { Error } from '../../../components/error/error'
import { PaddingWrapper } from '../../../components/paddingWrapper/paddingWrapper'
import { AdType } from '../../../model/ad'
import React = require('react')

export const CreateAdContainer = () => {
    const navigate = useNavigate()
    const [error, setError] = useState<IError>({ error: false, message: '' })

    const saveAd = (event) => {
        event.preventDefault()
        const request = {
            theme: event.target.elements.theme.value,
            text: event.target.elements.description.value,
            categoryId: event.target.category.value,
            type: AdType.FREE,
        }
        createAd(request).then((response) => {
            if (response.success) {
                navigate(`/ad/${response.data}`)
            } else {
                setError({
                    error: true,
                    message: response.error?.message,
                })
            }
        })
    }

    return (
        <>
            {error.error ? (
                <Error message={error.message} />
            ) : (
                <form onSubmit={saveAd}>
                    <PaddingWrapper padding={50}>
                        <Grid container direction={'column'}>
                            <Grid item>
                                <AdInfo ad={undefined} isEdit={true} />
                            </Grid>
                            <Grid item>
                                <Button type='submit'>Сохранить</Button>
                            </Grid>
                        </Grid>
                    </PaddingWrapper>
                </form>
            )}
        </>
    )
}
