import { useEffect, useRef } from "react"
import "functional-extensions"
import { useRipple } from "../hooks/ripple"
import { useNavigateTo } from "../hooks/NavigateTo"
import type { Item } from "../itemsLoader"
import styles from "./ItemList.module.css"


type GridItemsProps = {
    baseUrl: string
    items: Item[]
    path?: string
    from: string
}

export default function GridItems({ baseUrl, items, path, from }: GridItemsProps) {
    const firstItemRef = useRef<HTMLDivElement>(null)
    const ripple = useRipple()
    const navigate = useNavigateTo()
    
    useEffect(() => {
        if (firstItemRef.current) {
            firstItemRef.current.focus()
        }
    }, [items]) // focus when files change

    const onItem = (item: string) => navigate(path ? `/${baseUrl}`.appendPath(path).appendPath(item) : `/${baseUrl}`.appendPath(item))
    const onItemList = (itemPath: string) => navigate(path ? `/${baseUrl}list`.appendPath(path).appendPath(itemPath) : `/${baseUrl}list`.appendPath(itemPath))

    const onItemClick = (item: Item) => {
        if (!item.isDirectory && item.file)
            if (baseUrl == "picture!!!!!")
                window.AndroidBridge.navigate("pictures")
            else
                onItem(item.file)
            
        else
            onItemList(item.name)
    }

    const focused = from.includes(".")
                    ? Math.max(items.findIndex(n => n.file?.endsWith(from)), 0)
                    : Math.max(items.findIndex(n => n.name == from), 0)

    return (
        <>
            {items.map((n, index) => (
                    <div className={`ripple-container${n.isDirectory ? " " + styles.folder : ""}`} onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown}
                        onClick={() => onItemClick(n)} key={n.name} tabIndex={0} ref={index === focused ? firstItemRef : null}>
                        {n.name}
                    </div>
                )
            )}
        </>
    )
}


