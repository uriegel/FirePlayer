package de.uriegel.fireplayer.ui

import android.content.res.Configuration
import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_UP
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import de.uriegel.fireplayer.R
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
//        val scrollState = rememberLazyGridState()
//        val scrollState = rememberLazyListState()
//        val coroutineScope = rememberCoroutineScope()

//        val columns = when (configuration.orientation) {
//            Configuration.ORIENTATION_LANDSCAPE -> 6
//            else -> 3
        //}
        LazyColumn(
            //columns = GridCells.Fixed(columns),
            content = {
                items(40) { index ->
                    LazyRow(
                        content = {
                            items(6) {
                                Card(
                                    modifier =
                                    Modifier
                                        .padding(5.dp)
                                        .aspectRatio(1.8f)
                                        //                                        .onKeyEvent {
                                        //                                            if (it.type == KeyEventType.KeyDown && it.key.nativeKeyCode == KEYCODE_DPAD_UP) {
                                        //                                                coroutineScope.launch {
                                        //                                                    scrollState.scrollToItem(35)
                                        //                                                }
                                        //                                                true
                                        //                                            } else
                                        //                                                false
                                        //                                        }
                                        .clickable {
                                            Toast
                                                .makeText(context, "Test ${index + it}", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                ) {
                                    Text("$text ${index + it}")
                                }
                            }
                        }
                    )
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    FirePlayerTheme {
        MainScreen()
    }
}
