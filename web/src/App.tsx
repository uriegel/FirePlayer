import { useEffect, useRef } from 'react'
import './App.css'
import { useRipple } from './hooks/ripple'

export default function App() {

	const fokusButton = useRef<HTMLButtonElement | null>(null)
	
	useEffect(() => fokusButton.current?.focus(), [])
	const ripple = useRipple()

	const onClick = (text: string) => window.AndroidBridge.postMessage(text)

	return (
		<div className="page">
			<div className="settings">
				<div className="ripple-container" onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown} tabIndex={4}
					onClick={() => onClick("Settings")}>
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

// TODO React Router for routing settings, video, photo, music pages
// TODO React Router transitions
// TODO React Settings: UI, saving in local storage
// TODO Video file view
// TODO Video player view

// TODO Welcome UI: 
// Title with an Image
// | Video +Image of a Film | Photo +Image of several Photos | Music + Image of a tape machine |
// Last viewed movies

// TODO Welcome UI Portrait: flex.direction: row
