import { useContext } from "react"
import { PicturesContext } from "./PicturesContext"

export function useOptionalPictures() {
    return useContext(PicturesContext) // may be null
}

export function usePictures() {
    const ctx = useContext(PicturesContext)
    if (!ctx)
        throw new Error("usePictures must be used inside ViewerProvider")
    return ctx
}
