import './App.css'

export default function App() {
	return (
		<div className='viewer'>
        	<video className='mediaPlayer' controls autoPlay src="http://roxy:9865/video/2010.mp4" />        
		</div>
	)
}

