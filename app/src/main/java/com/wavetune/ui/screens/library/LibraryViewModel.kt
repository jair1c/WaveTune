package com.wavetune.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavetune.data.model.Song
import com.wavetune.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LibraryTab { SONGS, ARTISTS, PLAYLISTS }

data class LibraryUiState(
    val songs: List<Song> = emptyList(),
    val artists: List<String> = emptyList(),
    val isScanning: Boolean = false,
    val searchQuery: String = "",
    val selectedTab: LibraryTab = LibraryTab.SONGS
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: MusicRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _isScanning = MutableStateFlow(false)
    private val _selectedTab = MutableStateFlow(LibraryTab.SONGS)

    val uiState: StateFlow<LibraryUiState> = combine(
        repository.allSongs,
        repository.artists,
        _query,
        _isScanning,
        _selectedTab
    ) { songs, artists, query, scanning, tab ->
        val filteredSongs = if (query.isBlank()) songs
        else songs.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.artist.contains(query, ignoreCase = true) ||
                    it.album.contains(query, ignoreCase = true)
        }
        val filteredArtists = if (query.isBlank()) artists
        else artists.filter { it.contains(query, ignoreCase = true) }

        LibraryUiState(
            songs = filteredSongs,
            artists = filteredArtists,
            isScanning = scanning,
            searchQuery = query,
            selectedTab = tab
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LibraryUiState())

    fun setQuery(q: String) { _query.value = q }
    fun setTab(tab: LibraryTab) { _selectedTab.value = tab }

    fun scanMusic() {
        viewModelScope.launch {
            _isScanning.value = true
            repository.scanLocalMusic()
            _isScanning.value = false
        }
    }

    fun getAlbumArtUri(albumId: Long) = repository.getAlbumArtUri(albumId)
    fun getSongUri(songId: Long) = repository.getSongUri(songId)
}
