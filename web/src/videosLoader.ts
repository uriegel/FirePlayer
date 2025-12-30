export type Videos = {
    directories: string[],
    files: string[]
}

export const getUrl = (relativeUrl: string) => `${localStorage.getItem("url") || ""}/${relativeUrl}`

export async function videosLoader() {
    return {
        videos: fetch(getUrl("video")).then(r => r.json() as Promise<Videos>)
    }
}