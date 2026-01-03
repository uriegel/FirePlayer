export {};

declare global {
    interface Window {
        AndroidBridge: {
            postMessage: (text: string) => void,
            setWelcome: (message: boolean) => void
            enterFullscreen: () => void
            exitFullscreen: () => void
        }
        onPlay?: () => void
        onPause?: () => void
        onStop?: () => void
        onBackPressed?: () => void
        onForward?: () => void
        onRewind?: () => void
        onFastForward?: () => void
        onFastRewind?: () => void
    }
}

export {}