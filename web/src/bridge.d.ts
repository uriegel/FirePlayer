export {};

declare global {
    interface Window {
        AndroidBridge: {
            postMessage: (text: string) => void,
            setWelcome: (message: boolean) => void
            enterFullscreen: () => void
            exitFullscreen: () => void
        }
        onBackPressed?: () => void
        onForward?: () => void
        onRewind?: () => void
        onFastForward?: () => void
        onFastRewind?: () => void
    }
}

export {}