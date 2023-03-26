import styled from 'styled-components'

export const Body = styled.div`
    background-color: #fbfcfb;
    min-height: 100vh;
    margin: 0;
`

export const HEADER_HEIGHT = 50

export const HeaderStyles = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: ${HEADER_HEIGHT}px;
    background-color: #fbfcfb;
`

export const FooterStyles = styled.div`
    min-height: 100px;
    background-color: #161b27;
`

export const Center = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
`

export const Space = styled.div`
    height: ${(props) => props.height || 30}px;
`

export const pointer = {
    cursor: 'pointer',
}
