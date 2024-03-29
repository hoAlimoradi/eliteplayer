package com.alimoradi.presentation.search

import android.content.Context
import com.alimoradi.core.MediaId
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.core.gateway.RecentSearchesGateway
import com.alimoradi.core.gateway.podcast.PodcastAlbumGateway
import com.alimoradi.core.gateway.podcast.PodcastArtistGateway
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.core.gateway.podcast.PodcastPlaylistGateway
import com.alimoradi.core.gateway.track.*
import com.alimoradi.presentation.R
import com.alimoradi.presentation.model.DisplayableAlbum
import com.alimoradi.presentation.model.DisplayableHeader
import com.alimoradi.presentation.model.DisplayableItem
import com.alimoradi.sharedandroid.extensions.assertBackground
import com.alimoradi.shared.mapListItem
import com.alimoradi.shared.startWithIfNotEmpty
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SearchDataProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val searchHeaders: SearchFragmentHeaders,
    private val folderGateway: FolderGateway,
    private val playlistGateway2: PlaylistGateway,
    private val songGateway: SongGateway,
    private val albumGateway: AlbumGateway,
    private val artistGateway: ArtistGateway,
    private val genreGateway: GenreGateway,
    // podcasts
    private val podcastPlaylistGateway: PodcastPlaylistGateway,
    private val podcastGateway: PodcastGateway,
    private val podcastAlbumGateway: PodcastAlbumGateway,
    private val podcastArtistGateway: PodcastArtistGateway,
    // recent
    private val recentSearchesGateway: RecentSearchesGateway

) {

    private val queryChannel = ConflatedBroadcastChannel("")

    fun updateQuery(query: String) {
        queryChannel.offer(query)
    }

    fun observe(): Flow<List<DisplayableItem>> {
        return queryChannel.asFlow().flatMapLatest { query ->
            if (query.isBlank()) {
                getRecents()
            } else {
                getFiltered(query)
            }
        }.assertBackground()
    }

    fun observeArtists(): Flow<List<DisplayableItem>> {
        return queryChannel.asFlow()
            .flatMapLatest { getArtists(it) }
            .assertBackground()
    }

    fun observeAlbums(): Flow<List<DisplayableItem>> {
        return queryChannel.asFlow()
            .flatMapLatest { getAlbums(it) }
            .assertBackground()
    }

    fun observeGenres(): Flow<List<DisplayableItem>> {
        return queryChannel.asFlow()
            .flatMapLatest { getGenres(it) }
            .assertBackground()
    }

    fun observePlaylists(): Flow<List<DisplayableItem>> {
        return queryChannel.asFlow()
            .flatMapLatest { getPlaylists(it) }
            .assertBackground()
    }

    fun observeFolders(): Flow<List<DisplayableItem>> {
        return queryChannel.asFlow()
            .flatMapLatest { getFolders(it) }
            .assertBackground()
    }

    private fun getRecents(): Flow<List<DisplayableItem>> {
        return recentSearchesGateway.getAll()
            .mapListItem { it.toSearchDisplayableItem(context) }
            .map { it.toMutableList() }
            .map {
                if (it.isNotEmpty()) {
                    it.add(
                        DisplayableHeader(
                            type = R.layout.item_search_clear_recent,
                            mediaId = MediaId.headerId("clear recent"),
                            title = ""
                        )
                    )
                    it.addAll(0, searchHeaders.recents)
                }
                it
            }
    }

    private fun getFiltered(query: String): Flow<List<DisplayableItem>> {
        return combine(
                getArtists(query).map { if (it.isNotEmpty()) searchHeaders.artistsHeaders(it.size) else it },
                getAlbums(query).map { if (it.isNotEmpty()) searchHeaders.albumsHeaders(it.size) else it },
                getPlaylists(query).map { if (it.isNotEmpty()) searchHeaders.playlistsHeaders(it.size) else it },
                getGenres(query).map { if (it.isNotEmpty()) searchHeaders.genreHeaders(it.size) else it },
                getFolders(query).map { if (it.isNotEmpty()) searchHeaders.foldersHeaders(it.size) else it },
                getSongs(query)
            ) { list -> list.toList().flatten() }
    }

    private fun getSongs(query: String): Flow<List<DisplayableItem>> {
        return songGateway.observeAll().map {
            it.asSequence()
                .filter {
                    it.title.contains(query, true) ||
                            it.artist.contains(query, true) ||
                            it.album.contains(query, true)
                }.map { it.toSearchDisplayableItem() }
                .toList()
        }.combine(
            podcastGateway.observeAll().map {
                it.asSequence()
                    .filter {
                        it.title.contains(query, true) ||
                                it.artist.contains(query, true) ||
                                it.album.contains(query, true)
                    }.map { it.toSearchDisplayableItem() }
                    .toList()
            }
        ) { track, podcast ->
            val result = (track + podcast).sortedBy { it.title }
            result.startWithIfNotEmpty(searchHeaders.songsHeaders(result.size))
        }
    }

    private fun getAlbums(query: String): Flow<List<DisplayableItem>> {
        return albumGateway.observeAll().map {
            if (query.isBlank()) {
                return@map listOf<DisplayableItem>()
            }
            it.asSequence()
                .filter {
                    it.title.contains(query, true) ||
                            it.artist.contains(query, true)
                }.map { it.toSearchDisplayableItem() }
                .toList()
        }.combine(
            podcastAlbumGateway.observeAll().map {
                if (query.isBlank()) {
                    return@map listOf<DisplayableItem>()
                }
                it.asSequence()
                    .filter {
                        it.title.contains(query, true) ||
                                it.artist.contains(query, true)
                    }.map { it.toSearchDisplayableItem() }
                    .toList()
            }
        ) { track, podcast ->
            (track + podcast)
                .filterIsInstance<DisplayableAlbum>() // elsewhere the compiler does not recognise type
                .sortedBy { it.title }
        }
    }

    private fun getArtists(query: String): Flow<List<DisplayableItem>> {
        return artistGateway.observeAll().map {
            if (query.isBlank()) {
                return@map listOf<DisplayableItem>()
            }
            it.asSequence()
                .filter {
                    it.name.contains(query, true)
                }.map { it.toSearchDisplayableItem() }
                .toList()
        }.combine(
            podcastArtistGateway.observeAll().map {
                if (query.isBlank()) {
                    return@map listOf<DisplayableItem>()
                }
                it.asSequence()
                    .filter {
                        it.name.contains(query, true)
                    }.map { it.toSearchDisplayableItem() }
                    .toList()
            }
        ) { track, podcast ->
            (track + podcast)
                .filterIsInstance<DisplayableAlbum>() // elsewhere the compiler does not recognise type
                .sortedBy { it.title }
        }
    }

    private fun getPlaylists(query: String): Flow<List<DisplayableItem>> {
        return playlistGateway2.observeAll().map {
            if (query.isBlank()) {
                return@map listOf<DisplayableItem>()
            }
            it.asSequence()
                .filter {
                    it.title.contains(query, true)
                }.map { it.toSearchDisplayableItem() }
                .toList()
        }.combine(
            podcastPlaylistGateway.observeAll().map {
                if (query.isBlank()) {
                    return@map listOf<DisplayableItem>()
                }

                it.asSequence()
                    .filter {
                        it.title.contains(query, true)
                    }.map { it.toSearchDisplayableItem() }
                    .toList()
            }
        ) { track, podcast ->
            (track + podcast)
                .filterIsInstance<DisplayableAlbum>() // elsewhere the compiler does not recognise type
                .sortedBy { it.title }
        }
    }

    private fun getGenres(query: String): Flow<List<DisplayableItem>> {
        return genreGateway.observeAll().map {
            if (query.isBlank()) {
                return@map listOf<DisplayableItem>()
            }
            it.asSequence()
                .filter {
                    it.name.contains(query, true)
                }.map { it.toSearchDisplayableItem() }
                .toList()
        }
    }

    private fun getFolders(query: String): Flow<List<DisplayableItem>> {
        return folderGateway.observeAll().map {
            if (query.isBlank()) {
                return@map listOf<DisplayableItem>()
            }
            it.asSequence()
                .filter {
                    it.title.contains(query, true)
                }.map { it.toSearchDisplayableItem() }
                .toList()
        }
    }

}