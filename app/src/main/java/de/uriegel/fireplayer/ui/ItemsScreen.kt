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
import de.uriegel.fireplayer.extensions.isFilm
import de.uriegel.fireplayer.requests.getFilmList
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import kotlinx.coroutines.launch
import java.net.URLEncoder

@Composable
fun ItemsScreen(urlParts: MutableState<Array<String>>, itemsList: MutableState<List<String>>,
                padding: PaddingValues = PaddingValues()) {
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
        val coroutineScope = rememberCoroutineScope()
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            state = scrollState,
            content = {
                itemsIndexed(items = itemsList.value) { index, item ->
                    ListItem(item, modifier =
                        Modifier
                            .dpadNavigation(columns, scrollState, index)
                            .clickable {
                                if (item.isFilm()) {}
                                else {
                                    urlParts.value += URLEncoder.encode(item, "utf-8")
                                    coroutineScope.launch {
                                        getFilmList(urlParts.value).fold({
                                            itemsList.value = it
                                        }, {
                                            Toast
                                                .makeText(context, it.localizedMessage, Toast.LENGTH_LONG)
                                                .show()
                                        })
                                    }
                                }
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
    val urlParts: MutableState<Array<String>> = remember { mutableStateOf(arrayOf()) }
    FirePlayerTheme {
        ItemsScreen(urlParts, itemsList)
    }
}
