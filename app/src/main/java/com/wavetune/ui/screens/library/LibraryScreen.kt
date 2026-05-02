package com.wavetune.ui.screens.library

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlaylistPlay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.wavetune.data.model.Song
import com.wavetune.player.PlayerController
import com.wavetune.ui.components.SongRow

@Composable
fun LibraryScreen(
    playerController: PlayerController,
    onNavigateToPlayer: () -> Unit,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        LibraryHeader(
            query = state.searchQuery,
            onQueryChange = viewModel::setQuery,
            isScanning = state.isScanning,
            onScan = viewModel::scanMusic
        )

        // Tab row
        TabRow(state.selectedTab, onTabSelected = viewModel::setTab)

        // Content
        when (state.selectedTab) {
            LibraryTab.SONGS -> SongsTab(
                songs = state.songs,
                onSongClick = { song ->
                    playerController.playSongs(state.songs, state.songs.indexOf(song))
                    onNavigateToPlayer()
                },
                getAlbumArtUri = viewModel::getAlbumArtUri
            )
            LibraryTab.ARTISTS -> ArtistsTab(
                artists = state.artists,
                onArtistClick = { /* could push to artist detail screen */ }
            )
            LibraryTab.PLAYLISTS -> PlaylistsPlaceholder()
        }
    }
}

@Composable
private fun LibraryHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    isScanning: Boolean,
    onScan: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 60.dp, bottom = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Library",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            if (isScanning) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                TextButton(onClick = onScan) {
                    Text("Scan", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        // Search field
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text("Search songs, artists, albums…", style = MaterialTheme.typography.bodyMedium)
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp))
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TabRow(selected: LibraryTab, onTabSelected: (LibraryTab) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val tabs = listOf(
            LibraryTab.SONGS to "Songs",
            LibraryTab.ARTISTS to "Artists",
            LibraryTab.PLAYLISTS to "Playlists"
        )
        items(tabs) { (tab, label) ->
            val isSelected = tab == selected
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.clickable { onTabSelected(tab) }
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                )
            }
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun SongsTab(
    songs: List<Song>,
    onSongClick: (Song) -> Unit,
    getAlbumArtUri: (Long) -> Any
) {
    if (songs.isEmpty()) {
        EmptyState(
            icon = "♪",
            title = "No songs found",
            subtitle = "Tap Scan to find music on your device"
        )
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        items(songs, key = { it.id }) { song ->
            SongRow(
                song = song,
                albumArtUri = getAlbumArtUri(song.albumId),
                onClick = { onSongClick(song) }
            )
            HorizontalDivider(
                modifier = Modifier.padding(start = 86.dp, end = 20.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
private fun ArtistsTab(artists: List<String>, onArtistClick: (String) -> Unit) {
    if (artists.isEmpty()) {
        EmptyState("🎤", "No artists found", "Tap Scan to discover artists")
        return
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 120.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(artists) { artist ->
            ArtistCard(artist = artist, onClick = { onArtistClick(artist) })
        }
    }
}

@Composable
private fun ArtistCard(artist: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = artist.firstOrNull()?.uppercase() ?: "?",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = artist,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PlaylistsPlaceholder() {
    EmptyState("♫", "Playlists coming soon", "Create playlists to organize your music")
}

@Composable
private fun EmptyState(icon: String, title: String, subtitle: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, style = MaterialTheme.typography.displayLarge)
            Spacer(Modifier.height(16.dp))
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
