package de.uriegel.fireplayer.ui.image

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import de.uriegel.fireplayer.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImagePager(
    count: Int,
    loadAsync: suspend (Int)-> ByteArray?
) {
    val pagerState = rememberPagerState()
    HorizontalPager(count = count, state = pagerState) { pager ->
        Box(
            Modifier
            .fillMaxSize()) {
            AsyncImage(
                modifier = Modifier.align(Alignment.Center),
                contentDescription = "Image",
                loadAsync = { loadAsync(pager) }
            )
        }
    }
}

@Composable
fun AsyncImage(
    modifier: Modifier = Modifier,
    loadAsync: suspend ()-> ByteArray?,
    contentDescription: String,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var angle by remember { mutableFloatStateOf(0f) }
    val bitmap = remember {
        mutableStateOf(BitmapFactory.decodeResource(context.resources, R.drawable.emptypics))
    }

    LaunchedEffect(true) {
        scope.launch {
            loadAsync()?.apply {
            val imageData = loadImageData(this)
                angle = imageData.angle
                bitmap.value = imageData.bitmap
            }
        }
    }

    Image(
        modifier = modifier, //.rotate(angle),
        bitmap = bitmap.value.asImageBitmap(),
        contentDescription = contentDescription,
    )
}
