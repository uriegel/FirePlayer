import { createContext } from "react"
import type { Item } from "../itemsLoader"

export type PicturesContextType = {
  	images: string[],
	initialize: (subPath: string|undefined, items: Item[]) => void,
	path: string		
}

export const PicturesContext = createContext<PicturesContextType | null>(null)