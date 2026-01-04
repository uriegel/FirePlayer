export {};

declare global {
    interface Window {
        AndroidBridge: {
            postMessage: (text: string) => void,
            setWelcome: (message: boolean) => void
            enterFullscreen: () => void
            exitFullscreen: () => void
            navigate: (route: string) => void
        }
        onPlay?: () => void
        onPause?: () => void
        onStop?: () => void
        onBackPressed?: () => void
        onRight?: () => void
        onLeft?: () => void
        onFastForward?: () => void
        onFastRewind?: () => void
    }
}

export {}