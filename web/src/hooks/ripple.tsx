import { useCallback } from "react"

export function useRipple() {
    return useCallback(
        (e: React.PointerEvent<HTMLElement>) => {
            const target = e.currentTarget
            const rect = target.getBoundingClientRect()

            const ripple = document.createElement("span")
            const size = Math.max(rect.width, rect.height)

            ripple.className = "ripple"
            ripple.style.width = ripple.style.height = `${size}px`
            ripple.style.left = `${e.clientX - rect.left - size / 2}px`
            ripple.style.top = `${e.clientY - rect.top - size / 2}px`

            target.appendChild(ripple)

            ripple.addEventListener("animationend", () => ripple.remove())
        }, [])
}
