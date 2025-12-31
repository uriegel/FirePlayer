import { useEffect, useRef } from "react"
import "functional-extensions"
import { useRipple } from "../hooks/ripple"
import { useNavigateTo } from "../hooks/NavigateTo"
import type { VideoItem } from "../videosLoader"

type VideoGridItemsProps = {
    videos: VideoItem[]
    path?: string
}

export default function VideoGridItems({ videos, path }: VideoGridItemsProps) {
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

    const onVideoClick = (video: VideoItem) => {
        console.log("video", video)
        if (!video.isDirectory && video.file)
            onVideo(video.file)
        else
            onVideoList(video.name)
    }

    return (
        <>
            {videos.map((n, index) => (
                    <div className={`ripple-container`} onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown}
                        onClick={() => onVideoClick(n)} key={n.name} tabIndex={0} ref={index === 0 ? firstItemRef : null}>
                        {n.name}
                    </div>
                )
            )}
        </>
    )
}


