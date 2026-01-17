import type React from "react"
import { useRef } from "react"

type useTouchType = {
    onHStart?: () => void
    onVStart?: () => void
    onHMove?: (diff: number) => void
    onVMove?: (diff: number) => void
}

export function useTouch({ onVStart, onHStart, onHMove, onVMove }: useTouchType) {

    const pageX = useRef(0)
    const pageY = useRef(0)
    const width = useRef(0)
    const height = useRef(0)
    const moveVertical = useRef<boolean>(null) 

    return {
        onStart: (e: React.TouchEvent) => { 
            pageX.current = e.changedTouches[0].pageX
            pageY.current = e.changedTouches[0].pageY
            height.current = e.currentTarget.clientHeight / 2
            width.current = e.currentTarget.clientWidth / 2
        },

        onMove: (e: React.TouchEvent) => { 
            const xDiff = e.changedTouches[0].pageX - pageX.current
            const vDiff = e.changedTouches[0].pageY - pageY.current
            if (moveVertical.current == null) {
                moveVertical.current = Math.abs(vDiff) > Math.abs(xDiff)
                if (moveVertical.current == false && onHStart)
                    onHStart()
                else if (moveVertical.current && onVStart)
                    onVStart()
            }

            if (moveVertical.current  == false && onHMove)
                onHMove(xDiff / width.current)
            else if (moveVertical.current && onVMove)
                onVMove(vDiff / height.current)
        },

        onEnd: () => moveVertical.current = null
    }
}