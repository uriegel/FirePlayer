import { useParams } from "react-router-dom"
import { useCallback, useEffect } from "react"
import { useNavigateTo } from "../hooks/NavigateTo"
import styles from "./Video.module.css"

export function Video() {
    const { '*': subPath } = useParams() 
    const navigate = useNavigateTo()

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(false)
    }, [])

    const navigateBack = useCallback(() => {
        const parent = subPath?.getParentPath()
        const path = subPath?.endsWith("/") ? subPath.substring(0, subPath.length-1) : subPath
        const url = parent
            ? `/videolist/${parent}`
            : path
            ? "/videolist"
            : "/"
        const search = new URLSearchParams({
            from: path?.getFileName() || "",
        }).toString()
        navigate(`${url}?${search}`, 0)
    }, [navigate, subPath])

    useEffect(() => {
        window.onBackPressed = navigateBack

        return () => {
            delete window.onBackPressed;
        }
    }, [navigateBack, subPath])

    return (
        <div className={styles.viewer}>
            <video className={styles.mediaPlayer} controls autoPlay src={`${localStorage.getItem("url") || ""}/video/${subPath}`} />         
        </div>
    )
}
