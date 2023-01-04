package de.uriegel.fireplayer.ui

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.uriegel.fireplayer.android.ComponentExActivity
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import de.uriegel.fireplayer.extensions.onKeyDown

class MainActivity : ComponentExActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FirePlayerTheme {
                val context = LocalContext.current
                val navController = rememberNavController()
                Surface(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .onKeyDown(context) { keyCode, _ ->
                                if (keyCode == KeyEvent.KEYCODE_MENU) {
                                    navController.navigate(NavRoutes.ShowSettings.route)
                                    true
                                } else
                                    false
                            },
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.MainScreen.route,
                    ) {
                        composable(NavRoutes.MainScreen.route) {
                            MainScreen(navController)
                        }
                        composable(NavRoutes.ShowSettings.route) {
                            ShowSettings(navController)
                        }
                        composable(NavRoutes.Dialog.route + "/{stringId}",
                            arguments = listOf(navArgument("stringId") { type = NavType.IntType })
                        ) {
                            StateDialog(navController, it.arguments?.getInt("stringId")!!)
                        }
                        composable(NavRoutes.Dialog2.route + "/{stringId}/{info}",
                            arguments = listOf(
                                navArgument("stringId") { type = NavType.IntType },
                                navArgument("info") { type = NavType.StringType })
                        ) {
                            StateDialog(
                                navController,
                                it.arguments?.getInt("stringId")!!,
                                it.arguments?.getString("info")!!)
                        }
                    }



//                    var displayMode by rememberSaveable { mutableStateOf(DisplayMode.Default) }
//                    val fullscreenMode = rememberSaveable { mutableStateOf(false) }
//                    var stateText by rememberSaveable { mutableStateOf("") }
//                    resetDisplayMode = { displayMode = DisplayMode.Default }
//
//                    val coroutineScope = rememberCoroutineScope()

//                    val lifecycleOwner = LocalLifecycleOwner.current
//                    DisposableEffect(lifecycleOwner) {
//                        val TODO timer = LifetimeTimer(context, coroutineScope)
//                        val observer = LifecycleEventObserver { _, event ->
//                            when (event) {
//                                Lifecycle.Event.ON_RESUME -> {
//                                    coroutineScope.launch {
//                                        if (displayMode != DisplayMode.Ok) {
//                                            initializeHttp(this@MainActivity)
//                                                .bind { accessDisk() }
//                                                .fold(
//                                                    {
//                                                        displayMode = DisplayMode.Ok
//                                                    },
//                                                    {
//                                                    }
//                                                )
//                                        }
//                                    }
//                                }
//                                Lifecycle.Event.ON_START -> timer.start()
//                                Lifecycle.Event.ON_STOP -> timer.cancel()
//                                else -> {}
//                            }
//                        }
//
//                        lifecycleOwner.lifecycle.addObserver(observer)
//
//                        onDispose {
//                            lifecycleOwner.lifecycle.removeObserver(observer)
//                        }
//                    }
//                    @Composable
//                    fun showContent(fullscreenMode: MutableState<Boolean>, padding: PaddingValues = PaddingValues()) {
//                        when (displayMode) {
//                            DisplayMode.Default -> StateDialog(R.string.initializing, padding = padding)
//                            DisplayMode.Ok -> MainScreen(fullscreenMode)
//                            DisplayMode.GeneralError -> StateDialog(R.string.general_error, stateText, padding = padding)
//                            DisplayMode.ConnectError -> StateDialog(R.string.connect_error, stateText, padding = padding)
//                            DisplayMode.UnknownHostError -> StateDialog(R.string.unknown_host_error, stateText, padding = padding)
//                            DisplayMode.SslError -> StateDialog(R.string.ssl_error, stateText, padding = padding)
//                            DisplayMode.ProtocolError -> StateDialog(R.string.protocol_error, stateText, padding = padding)
//                        }
//                    }
                }
            }
        }
    }
}

