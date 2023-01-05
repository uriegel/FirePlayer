package de.uriegel.fireplayer.android

import android.os.CountDownTimer
import de.uriegel.fireplayer.requests.diskNeeded
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LifetimeTimer(private val coroutineScope: CoroutineScope)
    : CountDownTimer(10L * 3600_000, 30_000) {

    override fun onTick(_n: Long) {
        coroutineScope.launch {
            diskNeeded().onFailure {
                //Toast.makeText(context, R.string.connection_lost, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFinish() {}
}

