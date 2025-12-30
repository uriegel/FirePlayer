import { useContext, useEffect, useRef, useState } from "react"
import { useNavigate } from "react-router-dom"
import { DialogContext, ResultType } from "web-dialog-react"
import { useRipple } from '../hooks/ripple'
import styles from "./Settings.module.css"

export default function Settings() {

    const focusButton = useRef<HTMLDivElement | null>(null)
    const navigate = useNavigate()
    const ripple = useRipple()
    const dialog = useContext(DialogContext)

    const [url, setUrl] = useState(localStorage.getItem("url") || "")

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

    const onUrl = async () => {
        const res = await dialog.show({
            text: "Url des Media-Servers eingeben",
            inputText: localStorage.getItem("url") || "",
            inputType: "url",
            btnOk: true,
            btnCancel: true,
            defBtnOk: true
        })
        if (res.result == ResultType.Ok && res.input) {
            localStorage.setItem("url", res.input)
            setUrl(res.input)
        }
    }

    return (
        <div className={styles.settings}>
            <h1>
                Einstellungen
            </h1>
            <h2>
                Url
            </h2>
            <div className={`ripple-container`} ref={focusButton} onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown}
                onClick={onUrl} tabIndex={0}>
                <div>
                    Die Url des Servers (z.B. http://domain)
                </div>
                <div className={styles.value}>
                    {url}
                </div>
            </div>
        </div>
    )
}
