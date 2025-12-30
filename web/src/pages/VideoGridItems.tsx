import { useEffect, useRef } from "react"

type VideoGridItemsProps = {
    files: string[]
}

export default function VideoGridItems({ files }: VideoGridItemsProps) {
    const firstItemRef = useRef<HTMLDivElement>(null)

    useEffect(() => {
        if (firstItemRef.current) {
            firstItemRef.current.focus()
        }
    }, [files]) // focus when files change

    return (
        <>
            {files.map((n, index) => (
                <div key={n} tabIndex={0} ref={index === 0 ? firstItemRef : null}>
                    {n}
                </div>
            ))}
        </>
    )
}


