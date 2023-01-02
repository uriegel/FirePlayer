package de.uriegel.fireplayer.ui

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import de.uriegel.fireplayer.extensions.dpadNavigation
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme

@Composable
fun ItemsScreen(itemsList: MutableState<List<String>>, padding: PaddingValues = PaddingValues()) {
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
                itemsIndexed(items = itemsList.value) { index, item ->
                    ListItem(item, modifier =
                        Modifier
                            .dpadNavigation(columns, scrollState, index)
                            .clickable {
                                Toast
                                    .makeText(context, "Test $index", Toast.LENGTH_SHORT)
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
    val itemsList: MutableState<List<String>> = remember { mutableStateOf(listOf(
        "Horror movies", "Action movies", "New Cinema", "Blaxploitation", "Apocalypse now.mp4",
        "Taxi Driver.mp4", "The Godfather.mp4", "One flew over the cuckoos nest.mp4"
    ))}
    FirePlayerTheme {
        ItemsScreen(itemsList)
    }
}
