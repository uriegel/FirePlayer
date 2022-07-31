package de.uriegel.fireplayer

import android.os.CountDownTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class LifetimeTimer : CountDownTimer(10 * 3600_000, 30_000), CoroutineScope {
    override val coroutineContext = Dispatchers.Main

    override fun onTick(_n: Long) {
        launch {
            diskNeeded()
        }
    }

    override fun onFinish() {}
}