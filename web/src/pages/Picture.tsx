import { useCallback, useEffect } from "react"
import { useParams } from "react-router-dom"
import styles from "./Picture.module.css"
import { useNavigateTo } from "../hooks/NavigateTo"

export function Picture() {
    const { '*': subPath } = useParams() 
    const navigate = useNavigateTo()

    console.log("subPath", subPath)

    useEffect(() => {
        if (window.AndroidBridge) {
            window.AndroidBridge.setWelcome(false)
            window.AndroidBridge.enterFullscreen()
        }
        return () => {
            if (window.AndroidBridge) {
                window.AndroidBridge.exitFullscreen()
            }
        }
    }, [])

    const navigateBack = useCallback(() => {
            const parent = subPath?.getParentPath()
            const path = subPath?.endsWith("/") ? subPath.substring(0, subPath.length-1) : subPath
            const url = parent
                ? `/picturelist/${parent}`
                : path
                ? "/picturelist"
                : "/"
            const search = new URLSearchParams({
                from: path?.getFileName() || "",
            }).toString()
            navigate(`${url}?${search}`, 0)
    }, [navigate, subPath])
    
    useEffect(() => {
        window.onBackPressed = navigateBack
        return () => { delete window.onBackPressed }
    }, [navigateBack, subPath])

    const getPictureUrl = useCallback(() => `${localStorage.getItem("url") || ""}/pics/${subPath}`, [subPath])

    return (
        <div className={styles.viewer}>
            <img className={styles.viewerImg} src={getPictureUrl()}></img>
        </div>        
    )
}

