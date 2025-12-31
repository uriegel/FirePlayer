import { useParams } from "react-router-dom"
import { useEffect } from "react"
import { useNavigateTo } from "../hooks/NavigateTo"
import styles from "./Video.module.css"

export function Video() {
    const { '*': subPath } = useParams() 
    const navigate = useNavigateTo()

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(false)
    }, [])

    useEffect(() => {
        window.onBackPressed = () => navigate("/videolist", 0) // TODO fill parent and select movie

        return () => {
            delete window.onBackPressed;
        }
    }, [navigate])    
    
    return (
        <div className={styles.viewer}>
            <video className={styles.mediaPlayer} controls autoPlay src={`${localStorage.getItem("url") || ""}/video/${subPath}`} />         
        </div>
    )
}
