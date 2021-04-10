package com.alimoradi.data.repository

import com.alimoradi.core.entity.PlayingQueueSong
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.PlayingQueueGateway
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.core.gateway.track.SongGateway
import com.alimoradi.core.interactor.UpdatePlayingQueueUseCaseRequest
import com.alimoradi.data.db.dao.PlayingQueueDao
import com.alimoradi.data.utils.assertBackground
import com.alimoradi.data.utils.assertBackgroundThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class PlayingQueueRepository @Inject constructor(
    private val playingQueueDao: PlayingQueueDao,
    private val songGateway: SongGateway,
    private val podcastGateway: PodcastGateway

) : PlayingQueueGateway {

    override fun getAll(): List<PlayingQueueSong> {
        try {
//            assertBackgroundThread()
            val playingQueue =
                playingQueueDao.getAllAsSongs(songGateway.getAll(), podcastGateway.getAll())
            if (playingQueue.isNotEmpty()) {
                return playingQueue
            }
            return songGateway.getAll().mapIndexed { index, song -> song.toPlayingQueueSong(index) }
        } catch (ex: SecurityException) {
            // sometimes this method is called without having storage permission
            ex.printStackTrace()
            return emptyList()
        }
    }

    override fun observeAll(): Flow<List<PlayingQueueSong>> {
        return playingQueueDao.observeAllAsSongs(songGateway, podcastGateway)
            .assertBackground()
    }

    override fun update(list: List<UpdatePlayingQueueUseCaseRequest>) {
        assertBackgroundThread()
        playingQueueDao.insert(list)
    }

    private fun Song.toPlayingQueueSong(progressive: Int): PlayingQueueSong {
        return PlayingQueueSong(
            this.copy(idInPlaylist = progressive),
            getMediaId()
        )
    }

}
