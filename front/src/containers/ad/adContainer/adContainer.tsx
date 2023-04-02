import React = require('react')
import { Button, Grid, useFormControl } from '@mui/material'
// @ts-ignore
import defaultPic from '/public/empty.png'
import { useEffect, useState } from 'react'
import { IAd } from '../../../model/ad'
import { findById, updateAd } from '../../../api/adRequests'
import { useParams } from 'react-router'
import { useDispatch } from 'react-redux'
import { setLoading } from '../../../store/slices/appSlice'
import { AdInfo } from '../../../components/ad/adInfo/adInfo'
import { PaddingWrapper } from '../../../components/paddingWrapper/paddingWrapper'
import { useKeycloak } from '@react-keycloak/web'
import { IError } from '../../../model/common'
import { Error } from '../../../components/error/error'

export const AdContainer = () => {
    const [ad, setAd] = useState<IAd>()
    const [isEdit, setIsEdit] = useState(false)
    useFormControl()
    const [error, setError] = useState<IError>({ error: false, message: '' })
    const { adId } = useParams()
    const dispatch = useDispatch()
    const { keycloak } = useKeycloak()

    useEffect(() => {
        dispatch(setLoading(true))
        findById(adId)
            .then((resp) => {
                if (!resp.success) {
                    setError({
                        error: true,
                        message: resp.error?.message,
                    })
                } else {
                    setAd(resp.data)
                }
            })
            .finally(() => {
                dispatch(setLoading(false))
            })
    }, [])

    const showEditIcon = () => !isEdit && keycloak.authenticated && ad.createUser === keycloak.tokenParsed?.email

    const handleUpdateAd = (event) => {
        event.preventDefault()

        const request = {
            id: ad.id,
            newTheme: event.target.elements.theme.value,
            newText: event.target.elements.description.value,
            newCategoryId: event.target.category.value,
        }

        updateAd(request).then((it) => {
            if (!it.success) {
                setError({
                    error: true,
                    message: it.error?.message,
                })
            }
        })
        setIsEdit(false)
    }

    return (
        <>
            {error.error ? (
                <Error message={error.message} />
            ) : (
                ad && (
                    <Grid container direction={'column'}>
                        {showEditIcon() && (
                            <Grid item>
                                <PaddingWrapper right={80}>
                                    <Button
                                        variant={'outlined'}
                                        sx={{ float: 'right' }}
                                        onClick={() => setIsEdit(true)}>
                                        Редактировать
                                    </Button>
                                </PaddingWrapper>
                            </Grid>
                        )}
                        <form onSubmit={handleUpdateAd}>
                            <Grid item>
                                <Grid container>
                                    <Grid item xs={3}>
                                        <Grid container justifyContent={'center'} alignItems={'center'}>
                                            <img src={defaultPic} />
                                        </Grid>
                                    </Grid>
                                    <Grid item xs={9}>
                                        <AdInfo ad={ad} isEdit={isEdit} />
                                    </Grid>
                                </Grid>
                            </Grid>
                            <Grid item>
                                {isEdit && (
                                    <Button variant={'contained'} type='submit'>
                                        Сохранить
                                    </Button>
                                )}
                            </Grid>
                        </form>
                    </Grid>
                )
            )}
        </>
    )
}
