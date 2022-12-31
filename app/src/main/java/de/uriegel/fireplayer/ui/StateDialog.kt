package de.uriegel.fireplayer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import de.uriegel.fireplayer.R

@Composable
fun StateDialog(stringId: Int, error: String = "", padding: PaddingValues = PaddingValues()) {
    var extendedVisible by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier =
        Modifier
            .padding(padding)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val (text, extended) = createRefs()

        Text(
            stringResource(stringId),
            modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(if (error.isNotEmpty()) extended.top else parent.bottom)
                    centerHorizontallyTo(parent)
                }
                .padding(horizontal = 30.dp)
        )
        if (error.isNotEmpty())
            Box(
                modifier = Modifier.constrainAs(extended) {
                    top.linkTo(text.bottom)
                    bottom.linkTo(parent.bottom)
                    centerHorizontallyTo(parent)
                }
            ) {
                if (!extendedVisible)
                    Button(
                        { extendedVisible = true}
                    ) {
                        Text(stringResource(R.string.extended))
                    }
                else
                    Text(error, modifier = Modifier.padding(horizontal = 30.dp))
            }
    }
}

@Preview(showSystemUi = false)
@Composable
fun StateDialogPreview() {
    StateDialog(R.string.initializing)
}

@Preview(showSystemUi = false)
@Composable
fun StateDialogErrorPreview() {
    StateDialog(R.string.general_error, "Das ist ein sehr langer Fehlertext, der hoffentlich mehrfach umgebrochen wird, daher ist er so extrem lang")
}
