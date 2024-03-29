package de.uriegel.fireplayer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.android.isTv
import de.uriegel.fireplayer.extensions.fromBase64
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateDialog(navController: NavHostController, stringId: Int, error64: String = "") {
    Scaffold(
        topBar = { if (!isTv()) CreateTopBar(navController) },
        content = { it ->
            var extendedVisible by remember { mutableStateOf(false) }
            val error = error64.fromBase64()

            ConstraintLayout(
                modifier =
                Modifier
                    .padding(it)
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
                                { extendedVisible = true }
                            ) {
                                Text(stringResource(R.string.extended))
                            }
                        else
                            Text(error, modifier = Modifier.padding(horizontal = 30.dp))
                    }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun StateDialogPreview() {
    val navController = rememberNavController()
    FirePlayerTheme {
        StateDialog(navController, R.string.initializing)
    }
}

@Preview(showSystemUi = true)
@Composable
fun StateDialogErrorPreview() {
    val navController = rememberNavController()
    FirePlayerTheme {
        StateDialog(
            navController,
            R.string.general_error,
            "Das ist ein sehr langer Fehlertext, der hoffentlich mehrfach umgebrochen wird, daher ist er so extrem lang"
        )
    }
}
