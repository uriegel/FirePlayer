package de.uriegel.fireplayer.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.extensions.fromBase64
import de.uriegel.fireplayer.extensions.getFilePath
import de.uriegel.fireplayer.extensions.isPicture
import de.uriegel.fireplayer.extensions.readAll
import de.uriegel.fireplayer.requests.getResponseStream
import de.uriegel.fireplayer.viewmodel.DirectoryItemsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@Composable
fun PhotoScreen(viewModel: DirectoryItemsViewModel, path64: String?) {
    val path = path64?.fromBase64() ?: ""
    val filePath = path.getFilePath()
    val items = viewModel.items
        .filter { it.name.isPicture() }
        .map { (filePath + it.name).replace("+", "%20") }
    if (false)
        ImageFadePager(
            count = items.size,
            loadAsync = { loadBitmap(items[it]) }
        )
    else
        ImagePager(
            count = items.size,
            loadAsync = { loadBitmap(items[it]) }
        )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImagePager(
    count: Int,
    loadAsync: suspend (Int)-> Bitmap?
) {
    val pagerState = rememberPagerState()
    HorizontalPager(count = count, state = pagerState) { pager ->
        Box(Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier.align(Alignment.Center),
                contentDescription = "Image",
                loadAsync = { loadAsync(pager) }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageFadePager(
    count: Int,
    loadAsync: suspend (Int)-> Bitmap?
) {
    val scope = rememberCoroutineScope()
    var secondContent by remember { mutableStateOf(false)}
    Box(modifier =  Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            Crossfade(
                targetState = secondContent,
                animationSpec = tween(durationMillis = 2000)
            ) {
                when (it) {
                    false -> {
                        AsyncImage(
                            modifier = Modifier.align(Alignment.Center),
                            contentDescription = "Image",
                            loadAsync = { loadAsync(0) }
                        )
                    }
                    true -> {
                        AsyncImage(
                            modifier = Modifier.align(Alignment.Center),
                            contentDescription = "Image",
                            loadAsync = { loadAsync(50) }
                        )
                    }
                }
            }
        }
        Button({
            scope.launch {
                secondContent = !secondContent
            }
        }) {
            Text("Weiter")
        }
    }
}

@Composable
fun AsyncImage(
    modifier: Modifier,
    loadAsync: suspend ()-> Bitmap?,
    contentDescription: String,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bitmap = remember {
        mutableStateOf(BitmapFactory.decodeResource(context.resources, R.drawable.emptypics))
    }

    LaunchedEffect(true) {
        scope.launch {
            loadAsync()?.apply {
                bitmap.value = this
            }
        }
    }

    Image(
        modifier = modifier,
        bitmap = bitmap.value.asImageBitmap(),
        contentDescription = contentDescription,
    )
}

suspend fun loadBitmap(url: String): Bitmap? =
    withContext(Dispatchers.IO) {
        return@withContext getResponseStream(url)
            .map {
                it.readAll()
            }
            .fold({
                BitmapFactory.decodeByteArray(it, 0, it.size)
            }, { null })
    }

