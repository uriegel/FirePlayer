import { useEffect, useRef } from "react"
import { useNavigate } from "react-router-dom"
import { useRipple } from '../hooks/ripple'
import styles from "./Settings.module.css"

export default function Settings() {

    const focusButton = useRef<HTMLDivElement | null>(null)
    const navigate = useNavigate()
    const ripple = useRipple()

    useEffect(() => focusButton.current?.focus(), [])

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(false)
    }, [])

    useEffect(() => {
        window.onBackPressed = () => navigate("/", { viewTransition: true })

        return () => {
            delete window.onBackPressed;
        }
    }, [navigate])

    return (
        <div className={styles.settings}>
            <h1>
                Einstellungen
            </h1>
            <h2>
                Url
            </h2>
            <div className={`ripple-container`} ref={focusButton} onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown} tabIndex={0}>
                <div>
                    Die Url des Servers (z.B. http://domain)
                </div>
                <div className={styles.value}>
                    http://roxy:9865
                </div>
            </div>
            <input type="url" placeholder="Gib die Url ein, Mann!" ></input>
        </div>
    )
}
