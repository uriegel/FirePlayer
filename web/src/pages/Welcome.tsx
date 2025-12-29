import { useEffect, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRipple } from '../hooks/ripple'
import './Welcome.css'
import 'functional-extensions'
import { delayAsync } from 'functional-extensions'

export default function Welcome() {

    const focusButton = useRef<HTMLDivElement | null>(null)

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(true)
    }, [])

    useEffect(() => focusButton.current?.focus(), [])

    const ripple = useRipple()

    const onClick = (text: string) => {
        if (window.AndroidBridge)
            window.AndroidBridge.postMessage(text)
    }

    const navigate = useNavigate()

    return (
        <div className="page">
            <div className="settings">
                <div className="ripple-container" onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown} tabIndex={4}
                    onClick={async () => {
                        await delayAsync(250)
                        navigate("/settings", { viewTransition: true })
                    }}>
                    =
                </div>
            </div>
            <div className="controls">
                <div className="ripple-container button" ref={focusButton} onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown} tabIndex={1}
                    onClick={() => onClick("Filme")}>
                    Filme
                </div>
                <div className="ripple-container button" onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown} tabIndex={2}
                    onClick={() => onClick("Fotos")}>
                    Fotos
                </div>
                <div className="ripple-container button" onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown} tabIndex={3}
                    onClick={() => onClick("Musik")}>
                    Musik
                </div>
                {/* 		<div className='viewer'>
                                    <video className='mediaPlayer' controls autoPlay src="http://roxy:9865/video/2010.mp4" />         */}
            </div>
        </div>
    )
}

