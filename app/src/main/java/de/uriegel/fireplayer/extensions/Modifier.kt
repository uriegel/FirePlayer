package de.uriegel.fireplayer.extensions

import android.content.Context
import android.view.KeyEvent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.lifecycle.compose.LocalLifecycleOwner

fun Modifier.onKeyDown(context: Context, onKeyDown: (keyCode: Int, event: KeyEvent?)->Boolean) = composed {
    val recentKeyEvent: MutableState<((Int, KeyEvent?)->Boolean)?> = remember { mutableStateOf(null) }
    val activity: MutableState<ComponentExActivity?> = remember { mutableStateOf(null) }
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner) {
        context
            .findActivityEx()
            ?.also {
                activity.value = it
                recentKeyEvent.value = it.keyEvent
                it.keyEvent = onKeyDown
            }
        onDispose {
            if (recentKeyEvent.value != null && activity.value != null)
                activity.value!!.keyEvent = recentKeyEvent.value!!
        }
    }
    this
}