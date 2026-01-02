import { useParams } from "react-router-dom"
import { useCallback, useEffect, useRef } from "react"
import { useNavigateTo } from "../hooks/NavigateTo"
import styles from "./Video.module.css"

export function Video() {
    const { '*': subPath } = useParams() 
    const navigate = useNavigateTo()
    const videoRef = useRef<HTMLVideoElement>(null)

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(false)
    }, [])

    const onForward = useCallback(() => {
        if (videoRef.current) 
            videoRef.current.currentTime = (videoRef.current?.currentTime || 0) + 10
    }, [])

    const onRewind = useCallback(() => {
        if (videoRef.current) 
            videoRef.current.currentTime = (videoRef.current?.currentTime || 0) - 10
    }, [])

    const onFastForward = useCallback(() => {
        if (videoRef.current) 
            videoRef.current.currentTime = (videoRef.current?.currentTime || 0) + 120
    }, [])

    const onFastRewind = useCallback(() => {
        if (videoRef.current) 
            videoRef.current.currentTime = (videoRef.current?.currentTime || 0) - 120
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
        return () => { delete window.onBackPressed }
    }, [navigateBack, subPath])

    useEffect(() => {
        window.onForward = onForward
        window.onRewind = onRewind
        window.onFastForward = onFastForward
        window.onFastRewind = onFastRewind
        return () => {
            delete window.onForward
            delete window.onRewind
            delete window.onFastForward
            delete window.onFastRewind
        }
    }, [onForward, onRewind, onFastForward, onFastRewind])

    return (
        <div className={styles.viewer}>
            <video ref={videoRef} className={styles.mediaPlayer} controls autoPlay src={`${localStorage.getItem("url") || ""}/video/${subPath}`} />         
        </div>
    )
}
