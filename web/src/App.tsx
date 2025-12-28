import { useEffect, useRef } from 'react'
import './App.css'

export default function App() {

	const fokusButton = useRef<HTMLButtonElement | null>(null)
	
	useEffect(() => fokusButton.current?.focus(), [])

	const onClick = (text: string) => window.AndroidBridge.postMessage(text)

	return (
		<div className='viewer'>
			<button ref={fokusButton} onClick={() => onClick("Button 1")}>Erste Aktion</button>
			<button onClick={() => onClick("Button 2")}>Zweite Aktion</button>
			<button onClick={() => onClick("Button 3")}>Dritte Aktion</button>
        	<video className='mediaPlayer' controls autoPlay src="http://roxy:9865/video/2010.mp4" />        
		</div>
	)
}

