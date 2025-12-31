import { Suspense, useEffect } from "react"
import { Await, useLoaderData, useParams } from "react-router-dom"
import "functional-extensions"
import type { Videos } from "../videosLoader"
import VideoGridItems from "./VideoGridItems"
import { useNavigateTo } from "../hooks/NavigateTo"
import styles from "./VideoList.module.css"

export default function VideoList() {
    const { '*': subPath } = useParams() // catch-all parameter
    console.log("subPath", subPath)
    
    const navigate = useNavigateTo()
    const { videos } = useLoaderData() as { videos: Promise<Videos> }

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(false)
    }, [])
    
    useEffect(() => {
        window.onBackPressed = () => navigate("/", 0)

        return () => {
            delete window.onBackPressed;
        }
    }, [navigate])

    return (
        <Suspense fallback={<p>Lade Filme...</p>}>
            <Await resolve={videos}>
                { videos => (
                    <div className={styles.container}>
                        <VideoGridItems files={videos.files} path={subPath} />
                    </div>
                )}
            </Await>
        </Suspense>
    )
}