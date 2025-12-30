import { Suspense } from "react"
import { Await, useLoaderData } from "react-router-dom"
import "functional-extensions"
import type { Videos } from "../videosLoader"
//import styles from "./VideoList.module.css"

export default function VideoList() {
    const { videos } = useLoaderData() as {
        videos: Promise<Videos>
    }

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