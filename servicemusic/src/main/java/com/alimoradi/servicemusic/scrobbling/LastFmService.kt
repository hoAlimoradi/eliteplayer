package com.alimoradi.servicemusic.scrobbling

import de.umass.lastfm.Authenticator
import de.umass.lastfm.Caller
import de.umass.lastfm.Session
import de.umass.lastfm.Track
import de.umass.lastfm.scrobble.ScrobbleData
import com.alimoradi.core.entity.UserCredentials
import com.alimoradi.servicemusic.BuildConfig
import com.alimoradi.servicemusic.model.MediaEntity
import com.alimoradi.shared.CustomScope
import kotlinx.coroutines.*
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File
import java.util.logging.Level
import javax.inject.Inject

internal class LastFmService @Inject constructor(): CoroutineScope by CustomScope(Dispatchers.IO) {

    companion object {
        const val SCROBBLE_DELAY = 10L * 1000 // millis
    }

    private var session: Session? = null
    private var userCredentials: UserCredentials? = null

    private var scrobbleJob: Job? = null

    init {
        Caller.getInstance().userAgent = "com.alimoradi.eliteplayer"
        Caller.getInstance().logger.level = Level.OFF
    }

    fun tryAuthenticate(credentials: UserCredentials) {
        try {
            session = Authenticator.getMobileSession(
                credentials.username,
                credentials.password,
                BuildConfig.LAST_FM_KEY,
                BuildConfig.LAST_FM_SECRET
            )
            userCredentials = credentials
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }

    fun dispose(){
        scrobbleJob?.cancel()
    }

    fun scrobble(entity: MediaEntity){
        if (session == null || userCredentials == null){
            return
        }

        scrobbleJob?.cancel()
        scrobbleJob = launch {
            delay(SCROBBLE_DELAY)
            val scrobbleData = entity.toScrollData()
            Track.scrobble(scrobbleData, session)
            Track.updateNowPlaying(scrobbleData, session)
        }
    }

    private fun MediaEntity.toScrollData(): ScrobbleData {
        val musicBrainzId = try {
            val audioFile = AudioFileIO.read(File(this.path))
            audioFile.tagAndConvertOrCreateAndSetDefault
            val tag = audioFile.tag
            tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID)
        } catch (ex: Throwable){
            null
        }

        return ScrobbleData(
            this.artist,
            this.title,
            (System.currentTimeMillis() / 1000).toInt(),
            this.duration.toInt(),
            this.album,
            null,
            musicBrainzId,
            this.trackNumber,
            null,
            true
        )
    }

}