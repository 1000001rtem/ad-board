import * as React from 'react'
import { Route, Routes } from 'react-router-dom'
import { Header } from './containers/header/header'
import { Footer } from './containers/footer/footer'
import { Body, HEADER_HEIGHT } from './app.styled'
import { Menu } from './containers/menu/menu'
import { Main } from './containers/main/main'
import { Grid } from '@mui/material'
import { useSelector } from 'react-redux'
import { RootState } from './store/store'
import { Loader } from './components/loader/loader'
import { CreateAdContainer } from './containers/ad/createAdContainer/createAdContainer'
import { AdContainer } from './containers/ad/adContainer/adContainer'
import { CategoryContainer } from './containers/category/categoryContainer/categoryContainer'

function App() {
    const { loading } = useSelector((state: RootState) => {
        return {
            loading: state.app.loading,
        }
    })

    return (
        <>
            <Body>
                <Grid container spacing={2} marginTop={0} alignItems={'stretch'} sx={{ minHeight: 'inherit' }}>
                    <Grid container xs={2}>
                        <Menu />
                    </Grid>
                    <Grid container direction={'column'} xs={10}>
                        <Grid item sx={{ height: `${HEADER_HEIGHT}px` }}>
                            <Header />
                        </Grid>
                        <Grid item>
                            {loading && <Loader />}
                            <Routes>
                                <Route path={'/'} element={<Main />} />
                                <Route path={'/category/:categoryId'} element={<CategoryContainer />} />
                                <Route path={'/ad/:adId'} element={<AdContainer />} />
                                <Route path={'/ad/create-ad'} element={<CreateAdContainer />} />
                            </Routes>
                        </Grid>
                    </Grid>
                </Grid>
            </Body>
            <Footer />
        </>
    )
}

export default App
