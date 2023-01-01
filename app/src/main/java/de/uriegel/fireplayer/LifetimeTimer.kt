package de.uriegel.fireplayer

import android.content.Context
import android.os.CountDownTimer
import android.widget.Toast
import de.uriegel.fireplayer.requests.diskNeeded
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LifetimeTimer(
    private val context: Context,
    private val coroutineScope: CoroutineScope)
    : CountDownTimer(10L * 3600_000, 30_000) {

    override fun onTick(_n: Long) {
        coroutineScope.launch {
            diskNeeded().onFailure {
                Toast.makeText(context, R.string.connection_lost, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFinish() {}
}

