import * as React from 'react'
import * as ReactDOM from 'react-dom'
import { BrowserRouter } from 'react-router-dom'
import App from './src/app'
import './index.css'
import { Provider } from 'react-redux'
import { store } from './src/store/store'
import { ReactKeycloakProvider } from '@react-keycloak/web'
import keycloak from './keycloak'

ReactDOM.render(
    <ReactKeycloakProvider authClient={keycloak}>
        <React.StrictMode>
            <Provider store={store}>
                <BrowserRouter>
                    <App />
                </BrowserRouter>
            </Provider>
        </React.StrictMode>
    </ReactKeycloakProvider>,
    document.getElementById('root')
)
