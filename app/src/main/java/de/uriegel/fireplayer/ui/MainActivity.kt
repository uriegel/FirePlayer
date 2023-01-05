package de.uriegel.fireplayer.ui

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.uriegel.fireplayer.android.ComponentExActivity
import de.uriegel.fireplayer.android.LifetimeTimer
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import de.uriegel.fireplayer.extensions.onKeyDown
import de.uriegel.fireplayer.extensions.toBase64

class MainActivity : ComponentExActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FirePlayerTheme {
                val context = LocalContext.current
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner) {
                    val timer = LifetimeTimer(coroutineScope)
                    val observer = LifecycleEventObserver { _, event ->
                        when (event) {
                            Lifecycle.Event.ON_START -> timer.start()
                            Lifecycle.Event.ON_STOP -> timer.cancel()
                            else -> {}
                        }
                    }

                    lifecycleOwner.lifecycle.addObserver(observer)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

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
                        startDestination = NavRoutes.Init.route,
                    ) {
                        composable(NavRoutes.Init.route) {
                            InitScreen(navController)
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
                        composable(NavRoutes.Folders.route) {
                            FolderSelectorScreen(navController)
                        }
                        composable(NavRoutes.ItemsRoot.route) {
                            ItemsScreen(
                                navController, "/video".toBase64())
                        }
                        composable(NavRoutes.Items.route + "/{path}") {
                            ItemsScreen(
                                navController,
                                it.arguments?.getString("path"))
                        }
                        composable(NavRoutes.Video.route + "/{path}") {
                           VideoScreen(it.arguments?.getString("path"))
                       }
                    }
                }
            }
        }
    }
}

