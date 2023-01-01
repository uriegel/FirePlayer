package de.uriegel.fireplayer.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize

@Composable
fun MainScreen(padding: PaddingValues = PaddingValues()) {
    Box(
        Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                val size = it.size.toSize()
                val test = size
        }

    ) {
        val configuration = LocalConfiguration.current
        val text =
            when (configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> "Landscape"
                else -> "Portrait"
            }
        Text(text)
    }
}

@Preview()
@Composable
fun MainScreenPreview() {
    MainScreen()
}
