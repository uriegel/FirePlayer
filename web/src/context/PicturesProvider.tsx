import { useState } from "react"
import { PicturesContext } from "./PicturesContext"
import type { Item } from "../itemsLoader"

export function PicturesProvider({ children }: { children: React.ReactNode }) {
	const [images, setImages] = useState<string[]>([])
	const [path, setPath] = useState("")
	const initialize = (subPath: string | undefined, images: Item[]) => { 
		setImages(images.map(n => n.file).filterNone())
		setPath(subPath || "")
	}

  	return (
    	<PicturesContext.Provider value={{ images, initialize, path }}>
      		{children}
    	</PicturesContext.Provider>
  	)
}