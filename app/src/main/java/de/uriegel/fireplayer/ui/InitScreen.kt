package de.uriegel.fireplayer.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.exceptions.HttpProtocolException
import de.uriegel.fireplayer.exceptions.NotInitializedException
import de.uriegel.fireplayer.extensions.bind
import de.uriegel.fireplayer.extensions.toBase64
import de.uriegel.fireplayer.requests.accessDisk
import de.uriegel.fireplayer.requests.initializeHttp
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

@Composable
fun InitScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    coroutineScope.launch {
                        initializeHttp(context)
                            .bind { accessDisk() }
                            .fold({ navController.navigate(NavRoutes.Dialog.route + "/${R.string.app_title}"){
                                popUpTo(NavRoutes.Init.route)
                            } }, {
                                // TODO StateDialog: navigate to closeDialog

                                when (it) {
                                    is NotInitializedException ->
                                        navController.navigate(NavRoutes.ShowSettings.route)
                                    is UnknownHostException ->
                                        showError(navController, R.string.unknown_host_error, it.localizedMessage)
                                    is ConnectException ->
                                        showError(navController, R.string.connect_error, it.localizedMessage)
                                    is SSLException ->
                                        showError(navController, R.string.ssl_error, it.localizedMessage)
                                    is HttpProtocolException ->
                                        showError(navController, R.string.protocol_error, it.localizedMessage)
                                    is CancellationException -> {}
                                    else ->
                                        showError(navController, R.string.general_error, it.localizedMessage)
                                }
                            })
                    }
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    StateDialog(navController, stringId = R.string.initializing)

//
//    NavHost(
//        navController = navController,
//        startDestination = NavRoutes.Home.route,
//    ) {
//        composable(NavRoutes.Home.route) {
//            ItemsScreen(navController = navController, "/video".toBase64())
//        }
//
//        composable(NavRoutes.ListItems.route + "/{path}") {
//            val path = it.arguments?.getString("path")
//            ItemsScreen(navController = navController, path)
//        }
//
//        composable(NavRoutes.Video.route + "/{path}") {
//            val path = it.arguments?.getString("path")
//            VideoScreen(fullscreenMode, path)
//        }
//    }
}

fun showError(navController: NavHostController, stringId: Int, message: String?) =
    navController.navigate(
        NavRoutes.Dialog2.route
                + "/${stringId}/${message?.toBase64()}")


@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    FirePlayerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val navController = rememberNavController()
            InitScreen(navController)
        }
    }
}
