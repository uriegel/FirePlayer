package de.uriegel.fireplayer.ui

import android.graphics.BitmapFactory
import android.widget.Toast
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
import de.uriegel.fireplayer.extensions.fromBase64
import de.uriegel.fireplayer.extensions.getFilePath
import de.uriegel.fireplayer.extensions.isPicture
import de.uriegel.fireplayer.viewmodel.DirectoryItemsViewModel
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.cache.PictureCache
import de.uriegel.fireplayer.cache.jpgToTempFile
import de.uriegel.fireplayer.extensions.bind
import de.uriegel.fireplayer.requests.getResponseStream
import kotlinx.coroutines.launch

@Composable
fun PhotoScreen(viewModel: DirectoryItemsViewModel, path64: String?) {
    val path = path64?.fromBase64() ?: ""
    val filePath = path.getFilePath()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val items = viewModel
        .items
        .filter { it.name.isPicture() }
        .map { (filePath + it.name).replace("+", "%20") }

    var photoCache by remember { mutableStateOf(PictureCache(scope, items)) }
    var bitmap by remember {
        mutableStateOf(BitmapFactory.decodeResource(context.resources, R.drawable.emptypics))
    }

    //BitmapFactory.decodeStream()

    Box(Modifier.fillMaxSize()) {
        Image(modifier = Modifier.align(Alignment.Center),
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "some useful description",
        )

        Button(
            modifier = Modifier.align(Alignment.TopCenter),
            onClick = {
                /*url = photos[photoIndex++]*/
                scope.launch {
                    val url = filePath + viewModel.items[2].name.replace("+", "%20")
                    getResponseStream(url)
                        .bind {
                            it.jpgToTempFile()
                        }
                        .fold({
                            try {
                                bitmap = BitmapFactory.decodeStream(it.inputStream())
                            } finally{
                                it.delete()
                            }
                        }, {
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        })
                }
            }) {
            Text(text = ">")
        }
    }
}