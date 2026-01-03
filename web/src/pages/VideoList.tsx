import { Suspense, useCallback, useEffect } from "react"
import { Await, useLoaderData, useParams } from "react-router-dom"
import "functional-extensions"
import type { Item } from "../itemsLoader"
import VideoGridItems from "./VideoGridItems"
import { useNavigateTo } from "../hooks/NavigateTo"
import styles from "./VideoList.module.css"

export default function VideoList() {
    const { '*': subPath } = useParams() // catch-all parameter
    const navigate = useNavigateTo()
    const { videos, from } = useLoaderData() as { videos: Promise<Item[]>, from: string }

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(false)
    }, [])

    const navigateBack = useCallback(() => {
        const parent = subPath?.getParentPath()
        const path = subPath?.endsWith("/") ? subPath.substring(0, subPath.length-1) : subPath
        const url = parent
            ? `/videolist/${parent}`
            : path
            ? "/videolist"
            : "/"
        const search = new URLSearchParams({
            from: path?.getFileName() || "",
        }).toString()
        navigate(`${url}?${search}`, 0)
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
                {
                    videos => {
                        console.log("schwein", videos)
                        return (
                            <div className={styles.container}>
                                <VideoGridItems videos={videos} path={subPath} from={from} />
                            </div>
                        )
                    }}
            </Await>
        </Suspense>
    )
}