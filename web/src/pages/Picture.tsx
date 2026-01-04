import { useCallback, useEffect, useState } from "react"
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

    const { images, path } = usePictures()
    const [pos, setPos] = useState(images.indexOf(subPath?.getFileName() || ""))

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
        const url = path
            ? `/picturelist/${path}`
            : "/"
        const search = new URLSearchParams({
            from: images[pos],
        }).toString()
        navigate(`${url}?${search}`, 0)
    }, [navigate, images, pos, path])
    
    const onNext = useCallback(
        (next: boolean) => setPos(p => next ? Math.min(p + 1, images.length - 1) : Math.max(p - 1, 0)),
    [images.length])

    useEffect(() => {
        window.onRight = () => onNext(true)
        window.onLeft = () => onNext(false)
        return () => {
            delete window.onRight
            delete window.onLeft
        }
    }, [onNext])

    useEffect(() => {
        window.onBackPressed = navigateBack
        return () => { delete window.onBackPressed }
    }, [navigateBack])

    const getPictureUrl = useCallback(
        () => `${localStorage.getItem("url") || ""}/pics/${path}`.appendPath(images[pos]),
    [images, path, pos])

    const onClick = (e: React.MouseEvent) => {
        const rect = e.currentTarget.getBoundingClientRect()
        const x = e.clientX - rect.left
        const left = e.currentTarget.clientWidth / 2 - x > 0 
        setPos(p => left ? Math.max(p - 1, 0) : Math.min(p + 1, images.length - 1))
    }

    return (
        <div className={styles.viewer}>
            <img className={styles.viewerImg} src={getPictureUrl()}  onClick={onClick} tabIndex={0}></img>
        </div>        
    )
}

