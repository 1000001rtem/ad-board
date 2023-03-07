import * as React from 'react'
import { Route, Routes } from 'react-router-dom'
import { Header } from './containers/header/Header'
import { Footer } from './containers/footer/Footer'
import { Body } from './app.styled'
import { Menu } from './containers/menu/menu'
import { Main } from './containers/main/main'
import { CircularProgress, Grid } from '@mui/material'
import { CategoryContainer } from './containers/categoryContainer/categoryContainer'
import { useSelector } from 'react-redux'
import { RootState } from './store/store'

function App() {
    const { loading } = useSelector((state: RootState) => {
        return {
            loading: state.app.loading,
        }
    })

    return (
        <>
            <Header />
            <Body>
                <Grid container spacing={2} alignItems={'stretch'} sx={{ minHeight: 'inherit' }}>
                    <Grid container xs={2} sx={{ backgroundColor: 'pink' }}>
                        <Menu />
                    </Grid>
                    <Grid container xs={10} sx={{ backgroundColor: 'peru' }}>
                        {loading ? (
                            <CircularProgress color={'inherit'} />
                        ) : (
                            <Routes>
                                <Route path={'/'} element={<Main />} />
                                <Route path={'/category/:categoryId'} element={<CategoryContainer />} />
                            </Routes>
                        )}
                    </Grid>
                </Grid>
            </Body>
            <Footer />
        </>
    )
}

export default App
