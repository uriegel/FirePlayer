import { useEffect, useRef } from "react"
import "functional-extensions"
import { useRipple } from "../hooks/ripple"
import { useNavigateTo } from "../hooks/NavigateTo"
import type { Item } from "../itemsLoader"
import styles from "./VideoList.module.css"


type VideoGridItemsProps = {
    videos: Item[]
    path?: string
    from: string
}

export default function VideoGridItems({ videos, path, from }: VideoGridItemsProps) {
    const firstItemRef = useRef<HTMLDivElement>(null)
    const ripple = useRipple()
    const navigate = useNavigateTo()
    
    useEffect(() => {
        if (firstItemRef.current) {
            firstItemRef.current.focus()
        }
    }, [videos]) // focus when files change

    const onVideo = (movie: string) => navigate(path ? "/video".appendPath(path).appendPath(movie) : "/video".appendPath(movie))
    const onVideoList = (videoPath: string) => navigate(path ? "/videolist".appendPath(path).appendPath(videoPath) : "/videolist".appendPath(videoPath))

    const onVideoClick = (video: Item) => {
        if (!video.isDirectory && video.file)
            onVideo(video.file)
        else
            onVideoList(video.name)
    }

    const focused = from.includes(".")
                    ? Math.max(videos.findIndex(n => n.file?.endsWith(from)), 0)
                    : Math.max(videos.findIndex(n => n.name == from), 0)

    return (
        <>
            {videos.map((n, index) => (
                    <div className={`ripple-container${n.isDirectory ? " " + styles.folder : ""}`} onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown}
                        onClick={() => onVideoClick(n)} key={n.name} tabIndex={0} ref={index === focused ? firstItemRef : null}>
                        {n.name}
                    </div>
                )
            )}
        </>
    )
}


