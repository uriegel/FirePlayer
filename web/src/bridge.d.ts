export {};

declare global {
    interface Window {
        AndroidBridge: {
            postMessage: (text: string) => void,
            setWelcome: (message: boolean) => void
        }
    }
}

export {}