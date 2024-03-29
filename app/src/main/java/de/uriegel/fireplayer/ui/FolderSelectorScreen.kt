package de.uriegel.fireplayer.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.android.isTv
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import de.uriegel.fireplayer.ui.theme.card

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderSelectorScreen(navController: NavHostController) {
    Scaffold(
        topBar = { if (!isTv()) CreateTopBar(navController) },
        content = {
            ConstraintLayout(
                modifier =
                Modifier
                    .padding(it)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                val folder1 = R.string.videos
                val route1 = NavRoutes.VideoRoot.route
                val folder2 = R.string.pics
                val route2 = NavRoutes.PhotoRoot.route
                val folder3 = R.string.music
                val route3 = NavRoutes.MusicRoot.route
                val (card1, card2, card3) = createRefs()
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Folder(folder1, modifier = Modifier
                            .constrainAs(card1) {
                                start.linkTo(parent.start)
                                end.linkTo(card2.start)
                                centerVerticallyTo(parent)
                            }
                    ) { navController.navigate(route1) }
                    Folder(folder2, modifier = Modifier
                            .constrainAs(card2) {
                                start.linkTo(card1.end)
                                end.linkTo(card3.start)
                                centerVerticallyTo(parent)
                            }
                    ) { navController.navigate(route2) }
                    Folder(folder3, modifier = Modifier
                            .constrainAs(card3) {
                                start.linkTo(card2.end)
                                end.linkTo(parent.end)
                                centerVerticallyTo(parent)
                            }
                    ) { navController.navigate(route3) }
                } else {
                    Folder(folder1, modifier = Modifier
                            .constrainAs(card1) {
                                top.linkTo(parent.top)
                                bottom.linkTo(card2.top)
                                centerHorizontallyTo(parent)
                            }
                    ) { navController.navigate(route1) }
                    Folder(folder2, modifier = Modifier
                            .constrainAs(card2) {
                                top.linkTo(card1.bottom)
                                bottom.linkTo(card3.top)
                                centerHorizontallyTo(parent)
                            }
                    ) { navController.navigate(route2) }
                    Folder(folder3,modifier = Modifier
                            .constrainAs(card3) {
                                top.linkTo(card2.bottom)
                                bottom.linkTo(parent.bottom)
                                centerHorizontallyTo(parent)
                            }
                    ) { navController.navigate(route3) }
                }
            }
        }
    )
}

@Composable
fun Folder(textId: Int, modifier: Modifier, onClick: ()->Unit) {
    Card(shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .padding(15.dp)
            .clickable { onClick() }
    ){
        Box(modifier = Modifier
            .background(card)
            .width(150.dp)
            .height(150.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(stringResource(textId))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FolderSelectorScreenPreview() {
    FirePlayerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            FolderSelectorScreen(navController)
        }
    }
}

@Preview(showSystemUi = true, device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Composable
fun FolderSelectorScreenPreviewLandscape() {
    FirePlayerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            FolderSelectorScreen(navController)
        }
    }
}
