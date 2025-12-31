import { delayAsync } from "functional-extensions"
import { useNavigate } from "react-router-dom"

export function useNavigateTo() {
    const navigate = useNavigate()

    return async (path: string, delay = 250) => {
        await delayAsync(delay)
        navigate(path, { viewTransition: true })
    }
}