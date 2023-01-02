package de.uriegel.fireplayer.ui

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import de.uriegel.fireplayer.extensions.dpadNavigation
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme

@Composable
fun MainScreen(padding: PaddingValues = PaddingValues()) {
    val context = LocalContext.current
    Box(Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
        val columns = when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 6
            else -> 3
        }
        val scrollState = rememberLazyGridState()

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            state = scrollState,
            content = {
                items(80) {
                    val text = if (it < 10) "$it" else "$it.mp4"
                    ListItem(text, modifier =
                        Modifier
                            .dpadNavigation(columns, scrollState, it)
                            .clickable {
                                Toast
                                    .makeText(context, "Test $it", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    )
                }
            })
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    FirePlayerTheme {
        MainScreen()
    }
}
