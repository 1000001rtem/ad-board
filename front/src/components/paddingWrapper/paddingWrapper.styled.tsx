import styled from 'styled-components'

export const PaddingWrapperBox = styled.div`
    width: 100%;
    height: 100%;
    padding: ${(props) => props.value || 30}px;
`
