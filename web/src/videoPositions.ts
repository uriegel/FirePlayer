const DB_NAME = "VideoPlayerDB"
const DB_VERSION = 1
const STORE_NAME = "videoPositions"

function openDB() {
    return new Promise<IDBDatabase>((resolve, reject) => {
        const request = indexedDB.open(DB_NAME, DB_VERSION)

        request.onupgradeneeded = (event: IDBVersionChangeEvent) => {
            const db = (event.target as IDBOpenDBRequest).result
            if (!db.objectStoreNames.contains(STORE_NAME)) {
                const store = db.createObjectStore(STORE_NAME, { keyPath: "movieId" })
                store.createIndex("lastWatched", "lastWatched", { unique: false })
            }
        }

        request.onsuccess = () => resolve(request.result)
        request.onerror = () => reject(request.error)
    })
}

export async function savePosition(movieId: string, position: number) {
    const db = await openDB()
    return new Promise<void>((resolve, reject) => {
        const tx = db.transaction(STORE_NAME, "readwrite")
        const store = tx.objectStore(STORE_NAME)

        const now = Date.now()
        store.put({ movieId, position, lastWatched: now })

        tx.oncomplete = () => resolve()
        tx.onerror = () => reject(tx.error)
    })
}

export async function getPosition(movieId: string) {
    const db = await openDB()
    return new Promise<number>((resolve, reject) => {
        const tx = db.transaction(STORE_NAME, "readonly")
        const store = tx.objectStore(STORE_NAME)

        const request = store.get(movieId)
        request.onsuccess = () => resolve(request.result?.position || 0)
        request.onerror = () => reject(request.error)
    })
}

export async function getAllPositions() {
    const db = await openDB()
    return new Promise((resolve, reject) => {
        const tx = db.transaction(STORE_NAME, "readonly")
        const store = tx.objectStore(STORE_NAME)

        const request = store.getAll()
        request.onsuccess = () => resolve(request.result)
        request.onerror = () => reject(request.error)
    })
}

export async function deletePosition(movieId: string) {
    const db = await openDB()
    return new Promise<void>((resolve, reject) => {
        const tx = db.transaction(STORE_NAME, "readwrite")
        const store = tx.objectStore(STORE_NAME)

        store.delete(movieId)

        tx.oncomplete = () => resolve()
        tx.onerror = () => reject(tx.error)
    })
}
