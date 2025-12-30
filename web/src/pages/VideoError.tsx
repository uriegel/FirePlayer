import { useRouteError } from "react-router-dom"

export function VideoError() {
    const error = useRouteError()

    console.error(error)

    return (
        <div>
            <h2>Could not load videos</h2>
            <p>Please try again later.</p>
        </div>
    )
}
