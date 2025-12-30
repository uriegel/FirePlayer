import { Suspense, useEffect } from "react"
import { Await, useLoaderData, useNavigate } from "react-router-dom"
import "functional-extensions"
import type { Videos } from "../videosLoader"
//import styles from "./VideoList.module.css"

export default function VideoList() {

    const navigate = useNavigate()
    const { videos } = useLoaderData() as { videos: Promise<Videos> }

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(false)
    }, [])
    
    useEffect(() => {
        window.onBackPressed = () => navigate("/", { viewTransition: true })

        return () => {
            delete window.onBackPressed;
        }
    }, [navigate])

    return (
        <Suspense fallback={<p>Lade Filme...</p>}>
            <Await resolve={videos}>
                { videos => (
                    <ul>
                        {videos.files.map(n => (
                            <li key={n}>{n}</li>
                        ))}
                    </ul>
                )}
            </Await>
        </Suspense>
    )
}