import { useCallback, useEffect, useRef } from "react"
import { Outlet, useParams } from "react-router-dom"
import { useNavigateTo } from "../hooks/NavigateTo"
import { usePictures } from "../context/getPicturesContext"
import { PicturesProvider } from "../context/PicturesProvider"
import styles from "./Picture.module.css"

export function PictureLayout() {
    return (
        <PicturesProvider>
            <Outlet />
        </PicturesProvider>
    )
}

export function Picture() {
    const { '*': subPath } = useParams() 
    const navigate = useNavigateTo()

    console.log("subPath", subPath)

    const pos = useRef(0)

    const { images } = usePictures()

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

    const onClick = () => {
        pos.current = pos.current + 1
        console.log("neues Bild", images[pos.current])
    }

    return (
        <div className={styles.viewer}>
            <img className={styles.viewerImg} src={getPictureUrl()}  onClick={onClick} tabIndex={0}></img>
        </div>        
    )
}

