package de.uriegel.fireplayer

import java.util.*
import kotlin.concurrent.schedule
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun delay(delay: Long) {
    return suspendCoroutine { continuation ->
        Timer("Async Delay", false).schedule(delay) {
            continuation.resume(Unit)
        }
    }
}

