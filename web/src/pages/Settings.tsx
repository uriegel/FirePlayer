import { useEffect } from "react"

export default function Settings() {

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(false)
    }, [])

    return (
        <div>
            Das sind die Einstellungen
        </div>
    )
}
