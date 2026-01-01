import type { LoaderFunctionArgs } from "react-router-dom"

type Videos = {
    directories: string[],
    files: string[]
}

export type VideoItem = {
    name: string,
    file?: string,
    isDirectory?: boolean
}

export const getUrl = (relativeUrl: string) => `${localStorage.getItem("url") || ""}/${relativeUrl}`

function getFilenameWithoutExtension(filename: string) {
    const pos = filename.lastIndexOf(".")
    return filename.substring(0, pos)
}

export async function videosLoader({ params, request }: LoaderFunctionArgs) {
    // params["*"] contains the subPath or undefined
    const subPath = params["*"] ?? ""

    const url = new URL(request.url)
    const from = url.searchParams.get("from") ?? ""

    const endpoint = subPath
        ? `video/${subPath}`
        : "video";


    return {
        videos: fetch(getUrl(endpoint))
                    .then(r => r.json() as Promise<Videos>)
                    .then(r => r.directories.map(n => ({
                        name: n,
                        isDirectory: true
                    } as VideoItem)).concat(r.files.map(n => ({
                        file: n,
                        name: getFilenameWithoutExtension(n)
                    })))),
        from
    }
}