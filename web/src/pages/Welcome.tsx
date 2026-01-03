import { useEffect, useRef } from 'react'
import { useRipple } from '../hooks/ripple'
import { useNavigateTo } from '../hooks/NavigateTo'
import styles from './Welcome.module.css'

export default function Welcome() {

    const focusButton = useRef<HTMLDivElement | null>(null)

    useEffect(() => {
        if (window.AndroidBridge)
            window.AndroidBridge.setWelcome(true)
    }, [])

    useEffect(() => focusButton.current?.focus(), [])

    const ripple = useRipple()

    const onClick = (text: string) => {
        if (window.AndroidBridge)
            window.AndroidBridge.postMessage(text)
    }

    const navigate = useNavigateTo()

    return (
        <div className={styles.page}>
            <div className={styles.settings}>
                <div className="ripple-container" onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown} tabIndex={4}
                    onClick={async () => navigate("/settings")}>
                    =
                </div>
            </div>
            <div className={styles.controls}>
                <div className={`ripple-container ${styles.button}`} ref={focusButton} onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown} tabIndex={1}
                    onClick={async () => navigate("/videolist")}>
                    Filme
                </div>
                <div className={`ripple-container ${styles.button}`} onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown} tabIndex={2}
                    onClick={async () => navigate("/picturelist")}>
                    Fotos
                </div>
                <div className={`ripple-container ${styles.button}`} onPointerDown={ripple.onPointerDown} onKeyDown={ripple.onKeyDown} tabIndex={3}
                    onClick={() => onClick("Musik")}>
                    Musik
                </div>
            </div>
        </div>
    )
}

