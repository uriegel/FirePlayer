import { useEffect } from "react"
import { useNavigate } from "react-router-dom"

export default function Settings() {

    const navigate = useNavigate()

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
        <div>
            Das sind die Einstellungen
        </div>
    )
}
