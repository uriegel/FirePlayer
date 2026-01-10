import styles from "./Volume.module.css"

export function Volume() {
    return (
        <div className={styles.volumeContainer}>
            <div className={styles.volume}>
                <div />
                <div className={styles.progress}/>
            </div>
        </div>
    )
}
