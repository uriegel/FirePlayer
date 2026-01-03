import { createContext } from "react"
import type { Item } from "../itemsLoader"

export type PicturesContextType = {
  	images: Item[];
  	setImages: (items: Item[]) => void;
}

export const PicturesContext = createContext<PicturesContextType | null>(null)