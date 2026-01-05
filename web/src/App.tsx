import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import WithDialog from 'web-dialog-react'
import Welcome from './pages/Welcome'
import Settings from './pages/Settings'
import ItemList from './pages/ItemList'
import { VideoError } from './pages/VideoError'
import { musicLoader, picturesLoader, videosLoader } from './itemsLoader'
import { Video } from './pages/Video'
import { Picture, PictureLayout } from './pages/Picture'

export default function App() {

	const router = createBrowserRouter([
		{
			path: "/",
			element: <Welcome />,
		}, {
			path: "settings",
			element: <Settings />,
		}, {
			id: "videolist",
			path: "videolist/*",
			element: <ItemList baseUrl='video' />,
			loader: videosLoader,
			errorElement: <VideoError />
		}, {
			id: "musiclist",
			path: "musiclist/*",
			element: <ItemList baseUrl='music' />,
			loader: musicLoader,
			errorElement: <VideoError />
		}, {
			path: "video/*",
			element: <Video />,
		},{
    		element: <PictureLayout />, // no path = layout-only route
    		children: [{
				id: "picturelist",
				path: "picturelist/*",
				element: <ItemList baseUrl='picture' />,
				loader: picturesLoader,
				errorElement: <VideoError />
			}, {
        		path: "picture/*",
        		element: <Picture />,
      		}],
  		}
	])

	return (
		<WithDialog>
			<RouterProvider router={router} />
		</WithDialog>
	)
}

// TODO Kotlin Picture control: respect Boundaries 0..max
// TODO Back from Kotlin Picture: select this image
// TODO Picture list page control

// TODO Music player
// TODO Music player switch TV off

// TODO Carousel open exception in VideoError

// TODO Show mp4 movies in Picture.tsx
// TODO Show mp4 movies from Kotlin Picture

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

// TODO Picture viewer with transitions
