package de.uriegel.fireplayer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class ActivityResult(
    val resultCode: Int,
    val data: Intent?
)

@SuppressLint("Registered")
open class ActivityEx : AppCompatActivity() {
    suspend fun activityRequest(intent: Intent): ActivityResult? {
        return suspendCoroutine { continuation ->
            this.continuation = continuation
            currentActivityRequest = ++seed
            startActivityForResult(intent, currentActivityRequest)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != currentActivityRequest)
            return
        continuation.resume(ActivityResult(resultCode, data))
    }

    private lateinit var continuation: Continuation<ActivityResult?>
    private var currentActivityRequest = 0

    companion object {
        private var seed = 0
    }
}

