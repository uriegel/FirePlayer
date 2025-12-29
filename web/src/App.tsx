import { useEffect, useRef } from 'react'
import './App.css'
import { useRipple } from './hooks/ripple'

export default function App() {

	const fokusButton = useRef<HTMLButtonElement | null>(null)
	
	useEffect(() => fokusButton.current?.focus(), [])
	const ripple = useRipple()

	const onClick = (text: string) => window.AndroidBridge.postMessage(text)

	return (
		<div className='viewer'>
			<button className="ripple-container" onPointerDown={ripple} ref={fokusButton} onClick={() => onClick("Button 1")}>Erste Aktion</button>
			<div className="ripple-container" onPointerDown={ripple} tabIndex={0} onClick={() => onClick("Button 2")}>Zweite Aktion</div>
			<button onClick={() => onClick("Button 3")}>Dritte Aktion</button>
        	<video className='mediaPlayer' controls autoPlay src="http://roxy:9865/video/2010.mp4" />        
		</div>
	)
}

