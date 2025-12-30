import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import './App.css'
import Welcome from './pages/Welcome'
import Settings from './pages/Settings'

export default function App() {

	const router = createBrowserRouter([
		{
			path: "/",
			element: <Welcome />,
		},
		{
			path: "settings",
			element: <Settings />,
		}
	])

	return (
		<RouterProvider router={router} />
	)
}

// TODO React don't show focus settings in Android phone	
// TODO React Settings: show input dialog (url instead of text!)
// TODO React Settings: UI, saving in local storage
// TODO Video file view
// TODO Video player view

// TODO Welcome UI: 
// Title with an Image
// | Video +Image of a Film | Photo +Image of several Photos | Music + Image of a tape machine |
// Last viewed movies

// TODO Welcome UI Portrait: flex.direction: row
