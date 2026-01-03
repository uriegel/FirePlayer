import { useState } from "react"
import { PicturesContext } from "./PicturesContext"
import type { Item } from "../itemsLoader"

export function PicturesProvider({ children }: { children: React.ReactNode }) {
  	const [images, setImages] = useState<Item[]>([])

  	return (
    	<PicturesContext.Provider value={{ images, setImages }}>
      		{children}
    	</PicturesContext.Provider>
  	)
}