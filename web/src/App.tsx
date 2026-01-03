import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import WithDialog from 'web-dialog-react'
import Welcome from './pages/Welcome'
import Settings from './pages/Settings'
import VideoList from './pages/VideoList'
import { VideoError } from './pages/VideoError'
import { videosLoader } from './videosLoader'
import { Video } from './pages/Video'

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
			id: "videolist",
			path: "videolist/*",
			element: <VideoList />,
			loader: videosLoader,
			errorElement: <VideoError />
		},
		{
			path: "video/*",
			element: <Video />,
		}
	])

	return (
		<WithDialog>
			<RouterProvider router={router} />
		</WithDialog>
	)
}

// TODO Picture viewer
// TODO Picture viewer with transitions

// TODO Music player
// TODO Music player switch TV off

// TODO Carousel open exception in VideoError

// TODO React Settings: show input dialog and then "zurück" doesn't close the dialog
// TODO React more beautiful focus settings in Android phone
// TODO Dialog: plain simple with "Abbrechen", "Übernehmen", styled colors

// TODO Welcome UI: 
// Title with an Image
// | Video +Image of a Film | Photo +Image of several Photos | Music + Image of a tape machine |
// Last viewed movies

// TODO Welcome UI Portrait: flex.direction: row

// TODO useWideViewPort?
// TODO loadWithOverviewMode ?

// TODO Navigate to new page: Settings
//    url
// 	  ------
//    Sony Bravia (later)

