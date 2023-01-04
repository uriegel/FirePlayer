package de.uriegel.fireplayer.ui

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import de.uriegel.fireplayer.extensions.*
import de.uriegel.fireplayer.requests.getFilmList
import kotlinx.coroutines.launch

@Composable
fun ItemsScreen(navController: NavHostController, path64: String?) {
    val context = LocalContext.current
    val path = path64!!.fromBase64()
    Box(Modifier.fillMaxSize()) {
        val columns = when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 6
            else -> 3
        }
        val itemsList: MutableState<List<String>> = rememberSaveable { mutableStateOf(listOf())}
        val scrollState = rememberLazyGridState()
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                getFilmList(path).fold({
                    itemsList.value = it
                }, {
                    Toast
                        .makeText(context, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            )}
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            state = scrollState,
            content = {
                itemsIndexed(items = itemsList.value) { index, item ->
                    ListItem(item, modifier =
                        Modifier
                            .dpadNavigation(columns, scrollState, index)
                            .clickable {
                                val route = if (item.isFilm()) NavRoutes.Video.route else NavRoutes.Items.route
                                navController.navigate(route  + "/" + "$path/$item".toBase64()) {
                                    popUpTo(NavRoutes.Items.route  + "/" + path.toBase64())
                                }
                            }
                    )
                }
            })
    }
}

