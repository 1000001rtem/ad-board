import styled from 'styled-components'

export const PaddingWrapperBox = styled.div`
    width: 100%;
    height: 100%;
    padding: ${(p) => p.top || p.padding || 0}px ${(p) => p.right || p.padding || 0}px
        ${(p) => p.bottom || p.padding || 0}px ${(p) => p.left || p.padding || 0}px;
`
