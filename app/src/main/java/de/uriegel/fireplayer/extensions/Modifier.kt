package de.uriegel.fireplayer.extensions

import android.content.Context
import android.view.KeyEvent
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import de.uriegel.fireplayer.ComponentExActivity
import kotlinx.coroutines.launch

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

fun Modifier.dpadNavigation(columns: Int, scrollState: LazyGridState, index: Int) = composed {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    onKeyEvent {
        if (it.type == KeyEventType.KeyDown) {
            if (it.key.nativeKeyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                val lastInfo = scrollState.layoutInfo.visibleItemsInfo.last()
                val info = scrollState.layoutInfo.visibleItemsInfo[index - scrollState.firstVisibleItemIndex]
                if (info.row == lastInfo.row - 1) {
                    coroutineScope.launch {
                        scrollState.scrollToItem(
                            index + scrollState.firstVisibleItemIndex - (info.row - 1) * columns)
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                    true
                } else
                    false
            }
            else if (it.key.nativeKeyCode == KeyEvent.KEYCODE_DPAD_UP) {
                val itemIndex = index - scrollState.firstVisibleItemIndex
                val firstInfo = scrollState.layoutInfo.visibleItemsInfo[0]
                val info = scrollState.layoutInfo.visibleItemsInfo[itemIndex]
                if (info.row == firstInfo.row && index >= columns) {
                    coroutineScope.launch {
                        scrollState.scrollToItem(index - columns)
                        focusManager.moveFocus(FocusDirection.Up)
                    }
                    true
                } else
                    false
            }
            else
                false
        }
        else
            false
    }
}
