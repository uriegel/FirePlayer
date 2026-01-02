import { useParams } from "react-router-dom"
import { useCallback, useEffect, useRef, useState } from "react"
import { useNavigateTo } from "../hooks/NavigateTo"
import styles from "./Video.module.css"

export function Video() {
    const { '*': subPath } = useParams() 
    const navigate = useNavigateTo()
    const videoRef = useRef<HTMLVideoElement>(null)
    const [forwardMode, setForwardMode] = useState(0)
    const [seekDirection, setSeekDirection] = useState(0)

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(false)
    }, [])

    const getSkipTime = (mode: number) => mode == 0
        ? 5
        : mode == 1
        ? 10
        : mode == 2
        ? 20
        : 50

    const onForward = useCallback(() => {
        if (videoRef.current) {
            videoRef.current.currentTime = Math.min(videoRef.current.currentTime + getSkipTime(forwardMode), videoRef.current.duration)
            if (videoRef.current.currentTime == videoRef.current.duration) 
                setForwardMode(0)
        }
    }, [forwardMode])

    const onRewind = useCallback(() => {
        if (videoRef.current) {
            videoRef.current.currentTime = Math.max(videoRef.current.currentTime - getSkipTime(forwardMode), 0)
            if (videoRef.current.currentTime == 0) 
                setForwardMode(0)
        }
    }, [forwardMode])

    const onFastForward = useCallback(() => {
        if (videoRef.current) 
            videoRef.current.currentTime = videoRef.current.currentTime + 120
    }, [])

    const onFastRewind = useCallback(() => {
        if (videoRef.current) 
            videoRef.current.currentTime = videoRef.current.currentTime - 120
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

    const timer = useRef(-1)

    useEffect(() => {
        clearInterval(timer.current)
        if (forwardMode != 0)
            timer.current = setInterval(() => seekDirection == 0 ? onForward(): onRewind(), 300)
        else 
            timer.current = -1
    }, [forwardMode, seekDirection, onForward, onRewind])

    useEffect(() => {
        if (videoRef.current)
            videoRef.current.muted = forwardMode != 0
    }, [forwardMode])

    const onKeyDown = useCallback((e: React.KeyboardEvent) => {
        if (e.key == "f") {
            if (seekDirection == 1) {
                setSeekDirection(0)
                setForwardMode(1) 
            }
            else
                setForwardMode(p => p < 3 ? p+1 : 0)
        }
        else if (e.key == "r") {
            if (seekDirection == 0) {
                setSeekDirection(1)
                setForwardMode(1)
            }
            else
                setForwardMode(p => p < 3 ? p+1 : 0)
        }
    }, [seekDirection])

    const getOverlay = () => {
        const getText = () => forwardMode == 1 && seekDirection == 0
            ? "> 1-fach"
            : forwardMode == 2 && seekDirection == 0
            ? ">> 2-fach"
            : forwardMode == 3 && seekDirection == 0
            ? ">>> 3-fach" 
            : forwardMode == 1 && seekDirection == 1
            ? "< 1-fach"
            : forwardMode == 2 && seekDirection == 1
            ? "<< 2-fach"
            : "<<< 3-fach"        
        return forwardMode != 0 && (<div className={styles.overlay}><div>{getText()}</div></div>)
    }

    return (
        <div className={styles.viewer} onKeyDown={onKeyDown}>
            <video ref={videoRef} className={styles.mediaPlayer} controls autoPlay src={`${localStorage.getItem("url") || ""}/video/${subPath}`} />         
            {getOverlay()}
        </div>
    )
}
