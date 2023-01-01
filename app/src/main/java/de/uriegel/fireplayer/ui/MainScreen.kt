package de.uriegel.fireplayer.ui

import android.content.res.Configuration
import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_UP
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreen(padding: PaddingValues = PaddingValues()) {
    val context = LocalContext.current
    Box(
        Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                val size = it.size.toSize()
                val test = size
            }

    ) {
        val configuration = LocalConfiguration.current
        val text =
            when (configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> "Landscape"
                else -> "Portrait"
            }
        val scrollState = rememberLazyGridState()
        val coroutineScope = rememberCoroutineScope()
        val focusManager = LocalFocusManager.current

        val columns = when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 6
            else -> 3
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            state = scrollState,
            content = {
                items(80) { index ->
                    Card(modifier =
                    Modifier
                        .padding(5.dp)
                        .aspectRatio(1.8f)
                        .onKeyEvent {
                            if (it.type == KeyEventType.KeyDown) {
                                if (it.key.nativeKeyCode == KEYCODE_DPAD_DOWN) {
                                    val itemIndex = index - scrollState.firstVisibleItemIndex
                                    val lastInfo = scrollState.layoutInfo.visibleItemsInfo.last()
                                    val info = scrollState.layoutInfo.visibleItemsInfo.get(itemIndex)
                                    if (info.row == lastInfo.row) {
                                        coroutineScope.launch {
                                            scrollState.scrollToItem(
                                                index + scrollState.firstVisibleItemIndex - (info.row - 1) * 6)
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }
                                        true
                                    } else
                                        false
                                }
                                else if (it.key.nativeKeyCode == KEYCODE_DPAD_UP) {
                                    val itemIndex = index - scrollState.firstVisibleItemIndex
                                    val firstInfo = scrollState.layoutInfo.visibleItemsInfo.get(0)
                                    val info = scrollState.layoutInfo.visibleItemsInfo.get(itemIndex)
                                    if (info.row == firstInfo.row && index >= 6) {
                                        coroutineScope.launch {
                                            scrollState.scrollToItem(index - 6)
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
                        .clickable {
                            Toast
                                .makeText(context, "Test $index", Toast.LENGTH_SHORT)
                                .show()
                        }
                    ) {
                        Text("$text $index")
                    }
                }
            })
    }
}

//fun Modifier.dpadNavigation(
//    scrollState: LazyGridState
//) = composed {
//
//}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    FirePlayerTheme {
        MainScreen()
    }
}
