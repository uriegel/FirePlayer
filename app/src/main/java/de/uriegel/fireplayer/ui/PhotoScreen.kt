package de.uriegel.fireplayer.ui

import android.graphics.BitmapFactory
import android.os.Environment
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
import de.uriegel.fireplayer.requests.getBaseUrl
import de.uriegel.fireplayer.viewmodel.DirectoryItemsViewModel
import de.uriegel.fireplayer.R

@Composable
fun PhotoScreen(viewModel: DirectoryItemsViewModel, path64: String?) {
    val path = path64?.fromBase64() ?: ""
    val filePath = path.getFilePath()
    val context = LocalContext.current

    var photoIndex by remember { mutableStateOf(0) }
    var bitmap by remember {
        mutableStateOf(BitmapFactory.decodeResource(context.resources, R.drawable.emptypics))
    }

    val root = Environment.getExternalStorageDirectory()
    //val bitmap = BitmapFactory.decodeFile("/images/01.jpg");


    val photos = viewModel
        .items
        .filter { it.name.isPicture() }
        .map { getBaseUrl() + (filePath + it.name).replace("+", "%20") }


    Box(Modifier.fillMaxSize()) {
        Image(modifier = Modifier.align(Alignment.Center),
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "some useful description",
        )

        Button(
            modifier = Modifier.align(Alignment.TopCenter),
            onClick = { /*url = photos[photoIndex++]*/ }) {
            Text(text = ">")
        }
    }
}