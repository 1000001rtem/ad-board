import * as React from 'react'
import { Route, Routes } from 'react-router-dom'
import { Header } from './containers/header/header'
import { Footer } from './containers/footer/footer'
import { Body } from './app.styled'
import { Menu } from './containers/menu/menu'
import { Main } from './containers/main/main'
import { Grid } from '@mui/material'
import { CategoryContainer } from './containers/categoryContainer/categoryContainer'
import { useSelector } from 'react-redux'
import { RootState } from './store/store'
import { Loader } from './components/loader/loader'
import { AdContainer } from './containers/adContainer/adContainer'

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
                <Grid container spacing={2} marginTop={0} alignItems={'stretch'} sx={{ minHeight: 'inherit' }}>
                    <Grid container xs={2} sx={{ backgroundColor: 'pink' }}>
                        <Menu />
                    </Grid>
                    <Grid container xs={10} sx={{ backgroundColor: 'peru' }}>
                        {loading && <Loader />}
                        <Routes>
                            <Route path={'/'} element={<Main />} />
                            <Route path={'/category/:categoryId'} element={<CategoryContainer />} />
                            <Route path={'/ad/:adId'} element={<AdContainer />} />
                        </Routes>
                    </Grid>
                </Grid>
            </Body>
            <Footer />
        </>
    )
}

export default App
