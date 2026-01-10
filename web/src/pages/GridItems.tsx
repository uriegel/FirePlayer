import { useCallback, useEffect, useRef } from "react"
import "functional-extensions"
import { useRipple } from "../hooks/ripple"
import { useNavigateTo } from "../hooks/NavigateTo"
import type { Item } from "../itemsLoader"
import styles from "./ItemList.module.css"
import { delayAsync } from "functional-extensions"


type GridItemsProps = {
    baseUrl: string
    items: Item[]
    path?: string
    from: string
}

export default function GridItems({ baseUrl, items, path, from }: GridItemsProps) {
    const itemRefs = useRef<Array<HTMLDivElement | null>>([])
    const ripple = useRipple()
    const navigate = useNavigateTo()
    
    const initiallyFocused = from.includes(".")
                    ? Math.max(items.findIndex(n => n.file?.endsWith(from)), 0)
                    : Math.max(items.findIndex(n => n.name == from), 0)

    useEffect(() => {
        const el = itemRefs.current[initiallyFocused]
        el?.focus()
    }, [items, initiallyFocused])

    const onSetFocusedImage = useCallback(
        (i: number) => {
            const delayRun = async () => {
                await delayAsync(100)
                const el = itemRefs.current[i]
                el?.focus()
            }
            delayRun()
        },
        [])

    useEffect(() => {
        window.onSetFocusedImage = i => onSetFocusedImage(i)
        return () => { delete window.onSetFocusedImage }
    }, [onSetFocusedImage])

    const onItem = (item: string) => navigate(path ? `/${baseUrl}`.appendPath(path).appendPath(item) : `/${baseUrl}`.appendPath(item))
    const onItemList = (itemPath: string) => navigate(path ? `/${baseUrl}list`.appendPath(path).appendPath(itemPath) : `/${baseUrl}list`.appendPath(itemPath))

    const onItemClick = (item: Item) => {
        if (!item.isDirectory && item.file)
            if (baseUrl == "picture")
                window.AndroidBridge.showPictures(`${localStorage.getItem("url") || ""}/pics`.appendPath(path || ""),
                    items.map(n => n.file).filterNone(), Math.max(0, items.findIndex(n => n.file == item.file)))
            else
                onItem(item.file)
            
        else
            onItemList(item.name)
    }

    return (
        <>
            {items.map((n, index) => (
                    <div key={n.name} className={`ripple-container${n.isDirectory ? " " + styles.folder : ""}`} onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown}
                    onClick={() => onItemClick(n)} tabIndex={0} ref={el => { itemRefs.current[index] = el }} >
                        {n.name}
                    </div>
                )
            )}
        </>
    )
}


