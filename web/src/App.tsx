import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import WithDialog from 'web-dialog-react'
import Welcome from './pages/Welcome'
import Settings from './pages/Settings'
import VideoList from './pages/VideoList'
import { VideoError } from './pages/VideoError'
import { videosLoader } from './videosLoader'
import './App.css'

export default function App() {

	const router = createBrowserRouter([
		{
			path: "/",
			element: <Welcome />,
		},
		{
			path: "settings",
			element: <Settings />,
		},
		{
			path: "videolist",
			element: <VideoList />,
			loader: videosLoader,
			errorElement: <VideoError />
		}
	])

	return (
		<WithDialog>
			<RouterProvider router={router} />
		</WithDialog>
	)
}

// TODO CORS on home-server only for specific hosts and only for fireplayer
// TODO In VideoList, set backpress = true => navigate to welcome
// TODO Video file view
// TODO Video player view
// TODO Carousel open exception in VideoError

// TODO React Settings: show input dialog and then "zurück" doesn't close the dialog
// TODO React more beautiful focus settings in Android phone
// TODO Dialog: plain simple with "Abbrechen", "Übernehmen", styled colors

// TODO Welcome UI: 
// Title with an Image
// | Video +Image of a Film | Photo +Image of several Photos | Music + Image of a tape machine |
// Last viewed movies

// TODO Welcome UI Portrait: flex.direction: row
