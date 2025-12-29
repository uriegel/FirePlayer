import { useEffect, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRipple } from '../hooks/ripple'
import './Welcome.css'

export default function Welcome() {

    const fokusButton = useRef<HTMLButtonElement | null>(null)

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(true)
    }, [])

    useEffect(() => fokusButton.current?.focus(), [])
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
                    onClick={() => navigate("/settings")}>
                    =
                </div>
            </div>
            <div className="controls">
                <div className="ripple-container button" onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown} tabIndex={1}
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

