export function useRipple() {
    const createRipple = (target: HTMLElement, x: number, y: number) => {
        const rect = target.getBoundingClientRect()
        const ripple = document.createElement("span")
        const size = Math.max(rect.width, rect.height)

        ripple.className = "ripple"
        ripple.style.width = ripple.style.height = `${size}px`
        ripple.style.left = `${x - size / 2}px`
        ripple.style.top = `${y - size / 2}px`

        target.appendChild(ripple)
        ripple.addEventListener("animationend", () => ripple.remove())
    }

    return {
        onPointerDown: (e: React.PointerEvent<HTMLElement>) => {
            const rect = e.currentTarget.getBoundingClientRect()
            createRipple(e.currentTarget, e.clientX - rect.left, e.clientY - rect.top )
        },
        onKeyDown: (e: React.KeyboardEvent<HTMLElement>) => {
            if (e.key !== "Enter" && e.key !== " ")
                return

            const target = e.currentTarget
            const rect = target.getBoundingClientRect()

            createRipple(target, rect.width / 2, rect.height / 2 )
        }
    }
}