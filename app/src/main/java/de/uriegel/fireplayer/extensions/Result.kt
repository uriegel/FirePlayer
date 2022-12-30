package de.uriegel.fireplayer.extensions

inline fun <R, T> Result<T>.bind(transform: (value: T) -> Result<R>): Result<R>
    = this.fold(
        {transform(it)},
        {Result.failure(it)}
    )

inline fun <T> Result<T>.sideEffect(sideEffect: (value: T) -> Unit): Result<T> {
    this.fold({
        sideEffect(it)
    }, {})
    return this
}
