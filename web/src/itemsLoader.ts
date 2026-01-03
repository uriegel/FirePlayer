import { type LoaderFunctionArgs } from "react-router-dom"

type Items = {
    directories: string[],
    files: string[]
}

export type Item = {
    name: string,
    file?: string,
    isDirectory?: boolean
}

export type ItemsResult = {
    items: Promise<Item[]>,
    from: string
}

export const getUrl = (relativeUrl: string) => `${localStorage.getItem("url") || ""}/${relativeUrl}`

function getFilenameWithoutExtension(filename: string) {
    const pos = filename.lastIndexOf(".")
    return filename.substring(0, pos)
}

export const videosLoader = (args: LoaderFunctionArgs) => itemsLoader("video", args)
export const picturesLoader = (args: LoaderFunctionArgs) => itemsLoader("pics", args)
export const musicLoader = (args: LoaderFunctionArgs) => itemsLoader("music", args)

function itemsLoader(baseUrl: string, { params, request }: LoaderFunctionArgs) {
    // params["*"] contains the subPath or undefined
    const subPath = params["*"] ?? ""

    const url = new URL(request.url)
    const from = url.searchParams.get("from") ?? ""

    const endpoint = subPath
        ? `${baseUrl}/${subPath}`
        : baseUrl


    return {
        items: fetch(getUrl(endpoint))
                    .then(r => r.json() as Promise<Items>)
                    .then(r => r.directories.map(n => ({
                        name: n,
                        isDirectory: true
                    } as Item)).concat(r.files.map(n => ({
                        file: n,
                        name: getFilenameWithoutExtension(n)
                    })))),
        from
    } as ItemsResult
}

