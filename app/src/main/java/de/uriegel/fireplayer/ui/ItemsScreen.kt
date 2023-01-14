package de.uriegel.fireplayer.ui

import android.content.res.Configuration
import android.util.Log
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
import de.uriegel.fireplayer.exceptions.HttpProtocolException
import de.uriegel.fireplayer.extensions.*
import de.uriegel.fireplayer.requests.*
import de.uriegel.fireplayer.viewmodel.DirectoryItemsViewModel
import kotlinx.coroutines.launch

@Composable
fun ItemsScreen(navController: NavHostController, viewModel: DirectoryItemsViewModel?, path64: String?) {
    val context = LocalContext.current
    val path = path64!!.fromBase64()
    Box(Modifier.fillMaxSize()) {
        val columns = when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 6
            else -> 3
        }
        val fileList: MutableState<List<String>> = rememberSaveable { mutableStateOf(listOf())}
        val dirList: MutableState<List<String>> = rememberSaveable { mutableStateOf(listOf())}
        val scrollState = rememberLazyGridState()
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                getItemList(path).fold({
                    fileList.value = it.files
                    dirList.value = it.directories
                }, {
                    if (it is HttpProtocolException)
                        navController.navigate(NavRoutes.Init.route)
                    else
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
                val items = getAsDirectoryItems(dirList.value, fileList.value)
                itemsIndexed(items) { index, item ->
                    ListItem(item, modifier =
                        Modifier
                            .dpadNavigation(columns, scrollState, index)
                            .clickable {
                                val route = if (item.name.isFilm())
                                    NavRoutes.Video.route
                                else if (item.name.isMusic()) {
                                    Log.i("PHOTO", "Set music view model")
                                    viewModel?.items = items
                                    NavRoutes.Music.route
                                }
                                else if (item.name.isPicture()) {
                                    Log.i("PHOTO", "Set view model")
                                    viewModel?.items = items
                                    NavRoutes.Photo.route
                                }
                                else if (item.isDirectory)
                                    NavRoutes.Items.route
                                else
                                    null
                                route?.let {
                                    navController.navigate(it  + "/" + "$path/${item.name}".toBase64()) {
                                        popUpTo(NavRoutes.Items.route  + "/" + path.toBase64())
                                    }
                                }
                            }
                    )
                }
            })
    }
}

