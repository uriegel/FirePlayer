package de.uriegel.fireplayer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.extensions.getTitle
import de.uriegel.fireplayer.extensions.isFolder

@Composable
fun FolderItem(item: String) {
    Box(modifier = Modifier
        .paint(
            painterResource(id = R.drawable.folder),
            contentScale = ContentScale.Fit
        )
        .padding(12.dp)
        .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(item)
    }
}

@Composable
fun Item(item: String) {
    Box(modifier = Modifier
        .aspectRatio(1f)
        .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(item)
    }
}

@Composable
fun ListItem(item: String) {
    if (item.isFolder())
        FolderItem(item)
    else
        Item(item.getTitle())
}

@Preview(showSystemUi = false)
@Composable
fun ItemPreview() {
    Box(modifier = Modifier
        .width(100.dp)
        .height(100.dp)
    ) {
        ListItem("Films")
    }
}

@Preview(showSystemUi = false)
@Composable
fun FolderItemPreview() {
    Box(modifier = Modifier
        .width(100.dp)
        .height(100.dp)
    ) {
        ListItem("Film with a very long title.mp4")
    }
}
