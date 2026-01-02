export {};

declare global {
    interface Window {
        AndroidBridge: {
            postMessage: (text: string) => void,
            setWelcome: (message: boolean) => void
        }
        onBackPressed?: () => void
        onForward?: () => void
        onRewind?: () => void
    }
}

export {}