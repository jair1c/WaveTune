package com.wavetune.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wavetune.player.PlayerController
import com.wavetune.ui.components.MiniPlayerBar
import com.wavetune.ui.screens.library.LibraryScreen
import com.wavetune.ui.screens.player.PlayerScreen
import com.wavetune.ui.screens.welcome.WelcomeScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle

object Routes {
    const val WELCOME = "welcome"
    const val LIBRARY = "library"
    const val PLAYER = "player"
}

@Composable
fun WaveTuneNavGraph(playerController: PlayerController) {
    val navController = rememberNavController()
    val playerState by playerController.state.collectAsStateWithLifecycle()
    val currentRoute by navController.currentBackStackEntryAsState()

    val showMiniPlayer = playerState.currentSong != null &&
            currentRoute?.destination?.route != Routes.PLAYER &&
            currentRoute?.destination?.route != Routes.WELCOME

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showMiniPlayer,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                playerState.currentSong?.let { song ->
                    // need albumArt URI
                    val albumArtUri = android.content.ContentUris.withAppendedId(
                        android.net.Uri.parse("content://media/external/audio/albumart"),
                        song.albumId
                    )
                    MiniPlayerBar(
                        song = song,
                        albumArtUri = albumArtUri,
                        isPlaying = playerState.isPlaying,
                        onPlayPause = playerController::playOrPause,
                        onClick = { navController.navigate(Routes.PLAYER) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.WELCOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(
                Routes.WELCOME,
                exitTransition = { fadeOut(tween(400)) }
            ) {
                WelcomeScreen(
                    onGetStarted = {
                        navController.navigate(Routes.LIBRARY) {
                            popUpTo(Routes.WELCOME) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                Routes.LIBRARY,
                enterTransition = { fadeIn(tween(400)) }
            ) {
                LibraryScreen(
                    playerController = playerController,
                    onNavigateToPlayer = { navController.navigate(Routes.PLAYER) }
                )
            }

            composable(
                Routes.PLAYER,
                enterTransition = { fadeIn(tween(300)) + slideInVertically(tween(400)) { it / 2 } },
                exitTransition = { fadeOut(tween(300)) + slideOutVertically(tween(400)) { it / 2 } }
            ) {
                PlayerScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
