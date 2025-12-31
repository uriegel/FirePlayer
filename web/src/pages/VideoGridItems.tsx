import { useEffect, useRef } from "react"
import "functional-extensions"
import { useRipple } from "../hooks/ripple"
import { useNavigateTo } from "../hooks/NavigateTo"

type VideoGridItemsProps = {
    files: string[]
    path?: string
}

export default function VideoGridItems({ files, path }: VideoGridItemsProps) {
    const firstItemRef = useRef<HTMLDivElement>(null)
    const ripple = useRipple()
    const navigate = useNavigateTo()
    
    useEffect(() => {
        if (firstItemRef.current) {
            firstItemRef.current.focus()
        }
    }, [files]) // focus when files change

    const onVideo = (movie: string) => navigate(path ? "/video".appendPath(path).appendPath(movie) : "/video".appendPath(movie))

    return (
        <>
            {files.map((n, index) => (
                <div className={`ripple-container`} onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown}
                    onClick={() => onVideo(n)} key={n} tabIndex={0} ref={index === 0 ? firstItemRef : null}>
                    {n}
                </div>
            ))}
        </>
    )
}


