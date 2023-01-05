package de.uriegel.fireplayer.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import de.uriegel.fireplayer.extensions.fromBase64
import de.uriegel.fireplayer.extensions.toBase64
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import de.uriegel.fireplayer.viewmodel.MusicViewModel

@Composable
fun MusicScreen(viewModel: MusicViewModel, path64: String?) {
    val path = path64?.fromBase64() ?: ""
    Text(path)
}

@Preview(showSystemUi = true)
@Composable
fun MusicScreenPreview() {
    FirePlayerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            MusicScreen(viewModel(), "Musik.mp3".toBase64())
        }
    }
}

