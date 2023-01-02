package de.uriegel.fireplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.extensions.getTitle
import de.uriegel.fireplayer.extensions.isFolder
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import de.uriegel.fireplayer.ui.theme.card

@Composable
fun ListItem(item: String, modifier: Modifier = Modifier) {

    val isFolder = item.isFolder()
    val boxModifier =
        if (isFolder)
            Modifier
                .paint(
                    painterResource(id = R.drawable.folder),
                    contentScale = ContentScale.FillBounds)
                .padding(12.dp)
        else
            Modifier
                .padding(5.dp)

    Card(
        shape = RoundedCornerShape(10.dp),
        elevation=5.dp,
        modifier =
            modifier
                .aspectRatio(1.8f)
                .padding(5.dp)
    ) {
        Box(
            modifier = boxModifier.background(card),
            contentAlignment = Alignment.Center
        ) {
            Text(if (isFolder) item else item.getTitle(), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun TestItem(item: String) {
    FirePlayerTheme {
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        ) {
            ListItem(item)
        }
    }
}

@Preview(showSystemUi = false)
@Composable
fun FolderItemPreview() {
    TestItem("Films")
}

@Preview(showSystemUi = false)
@Composable
fun ItemPreview() {
    TestItem("Film with title.mp4")
}
