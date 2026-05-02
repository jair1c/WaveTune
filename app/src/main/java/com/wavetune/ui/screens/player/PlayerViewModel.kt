package com.wavetune.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavetune.player.PlayerController
import com.wavetune.player.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    val controller: PlayerController
) : ViewModel() {

    val state: StateFlow<PlayerState> = controller.state

    private var positionJob: Job? = null

    fun startPositionTracking(onPosition: (Long) -> Unit) {
        positionJob?.cancel()
        positionJob = viewModelScope.launch {
            while (isActive) {
                onPosition(controller.getCurrentPosition())
                delay(500)
            }
        }
    }

    fun stopPositionTracking() {
        positionJob?.cancel()
    }

    fun playOrPause() = controller.playOrPause()
    fun seekToNext() = controller.seekToNext()
    fun seekToPrevious() = controller.seekToPrevious()
    fun seekTo(position: Long) = controller.seekTo(position)
    fun toggleShuffle() = controller.toggleShuffle()
    fun toggleRepeat() = controller.toggleRepeat()
}
