import React = require('react')
import { PaddingWrapperBox } from './paddingWrapper.styled'

interface IProps {
    top?: number
    right?: number
    bottom?: number
    left?: number
    padding?: number
    children: any
}

export const PaddingWrapper = (props: IProps) => {
    const { padding, top, left, right, bottom, children } = props
    return (
        <PaddingWrapperBox padding={padding} top={top} left={left} right={right} bottom={bottom}>
            {children}
        </PaddingWrapperBox>
    )
}
