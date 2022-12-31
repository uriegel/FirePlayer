package de.uriegel.fireplayer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun StateDialog(stringId: Int, error: String = "", padding: PaddingValues = PaddingValues()) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(padding)
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        // TODO Preview
        // TODO ConstraintLayout
        // TODO if error show button

        Text(
            stringResource(stringId),
            modifier = Modifier.padding(horizontal = 30.dp))
    }
}
