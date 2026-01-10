export {};

declare global {
    interface Window {
        AndroidBridge?: {
            postMessage: (text: string) => void,
            setWelcome: (message: boolean) => void
            enterFullscreen: () => void
            exitFullscreen: () => void
            showPictures: (baseUrl: string, items: string[], index: number) => void
            isTv: () => boolean
        }
        onPlay?: () => void
        onPause?: () => void
        onStop?: () => void
        onBackPressed?: () => void
        onRight?: () => void
        onLeft?: () => void
        onFastForward?: () => void
        onFastRewind?: () => void
        onSetFocusedImage?: (index: number) => void
    }
}

export {}