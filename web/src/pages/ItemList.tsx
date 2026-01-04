import { Suspense, useCallback, useEffect } from "react"
import { Await, useLoaderData, useParams } from "react-router-dom"
import "functional-extensions"
import GridItems from "./GridItems"
import { useNavigateTo } from "../hooks/NavigateTo"
import styles from "./ItemList.module.css"
import type { ItemsResult } from "../itemsLoader"
import { useOptionalPictures } from "../context/getPicturesContext"

type ItemListProps = {
    baseUrl: string
}

export default function ItemList({ baseUrl }: ItemListProps) {
    const { '*': subPath } = useParams() // catch-all parameter
    const navigate = useNavigateTo()
    const { items, from } = useLoaderData() as ItemsResult
    const pictures = useOptionalPictures()

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(false)
    }, [])

    const navigateBack = useCallback(() => {
        const parent = subPath?.getParentPath()
        const path = subPath?.endsWith("/") ? subPath.substring(0, subPath.length-1) : subPath
        const url = parent
            ? `/${baseUrl}list/${parent}`
            : path
            ? `/${baseUrl}list`
            : "/"
        const search = new URLSearchParams({
            from: path?.getFileName() || "",
        }).toString()
        navigate(`${url}?${search}`, 0)
    }, [navigate, subPath, baseUrl])
    
    useEffect(() => {
        window.onBackPressed = navigateBack

        return () => {
            delete window.onBackPressed;
        }
    }, [navigateBack, subPath])

    return (
        <Suspense fallback={<p>Lade Filme...</p>}>
            <Await resolve={items}>
                {
                    items => {
                        pictures?.initialize(subPath, items)
                        return (
                            <div className={styles.container}>
                                <GridItems baseUrl={baseUrl} items={items} path={subPath} from={from} />
                            </div>
                        )
                    }
                }
            </Await>
        </Suspense>
    )
}