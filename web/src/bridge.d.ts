export {};

declare global {
    interface Window {
        AndroidBridge: {
            postMessage: (text: string) => void
        }
    }
}

export {}