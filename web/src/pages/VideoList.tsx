import { Suspense, useCallback, useEffect } from "react"
import { Await, useLoaderData, useParams } from "react-router-dom"
import "functional-extensions"
import type { VideoItem } from "../videosLoader"
import VideoGridItems from "./VideoGridItems"
import { useNavigateTo } from "../hooks/NavigateTo"
import styles from "./VideoList.module.css"

export default function VideoList() {
    const { '*': subPath } = useParams() // catch-all parameter
    const navigate = useNavigateTo()
    const { videos } = useLoaderData() as { videos: Promise<VideoItem[]> }

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(false)
    }, [])

    const navigateBack = useCallback(() => {
        const parent = subPath?.getParentPath()
        const url = parent
            ? `/videolist/${parent}`
            : subPath
            ? "/videolist"
            : "/"
        navigate(url , 0)
    }, [navigate, subPath])
    
    useEffect(() => {
        window.onBackPressed = navigateBack

        return () => {
            delete window.onBackPressed;
        }
    }, [navigateBack, subPath])

    return (
        <Suspense fallback={<p>Lade Filme...</p>}>
            <Await resolve={videos}>
                { videos => (
                    <div className={styles.container}>
                        <VideoGridItems videos={videos} path={subPath} />
                    </div>
                )}
            </Await>
        </Suspense>
    )
}