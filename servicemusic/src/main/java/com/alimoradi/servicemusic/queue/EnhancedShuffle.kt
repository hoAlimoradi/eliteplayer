package com.alimoradi.servicemusic.queue

import com.alimoradi.injection.dagger.PerService
import com.alimoradi.servicemusic.interfaces.IPlayerLifecycle
import com.alimoradi.servicemusic.model.MediaEntity
import com.alimoradi.servicemusic.model.MetadataEntity
import com.alimoradi.shared.removeFirst
import javax.inject.Inject
import kotlin.math.min

@PerService
internal class EnhancedShuffle @Inject constructor(
    playerLifecycle: IPlayerLifecycle

) : IPlayerLifecycle.Listener {

    /**
     * lastListened[listening_now, ,.., listened_long_ago]
     */
    private var lastListened = mutableListOf<Long>()

    init {
        playerLifecycle.addListener(this)
    }

    override fun onMetadataChanged(metadata: MetadataEntity) {
        val mediaEntity = metadata.entity
        lastListened.removeFirst { it == mediaEntity.id }
        lastListened.add(0, mediaEntity.id)
    }

    /*
     * The algorithm tries to move at the end of the queue recently played songs
     * (found in [lastListened])
     *
     * e.g.
     * Before shuffle:
     * lastListened[5,4,3,2,1]
     * @param list: [1,2,3,4,5,6,7,8,9,10]
     *
     *
     * After shuffle:
     * currentLastListened = [5,4,3,2,1]
     * @return [
     *      7,9,6,10,8, // random shuffle
     *      1,2,3,4,5   // last played songs in reverse order
     * ]
     */
    fun shuffle(list: List<MediaEntity>): List<MediaEntity> {
        val halfListSize = list.size / 2

        val currentLastListened = lastListened.take(
            min(lastListened.size, halfListSize)
        ).toMutableList()

        val shuffled = list.shuffled().toMutableList()

        for (l in currentLastListened.reversed()) {
            val index = shuffled.indexOfFirst { it.id == l }
            if (index in 0 until shuffled.size) {
                val item = shuffled[index]
                shuffled.removeAt(index)
                shuffled.add(item)
            }
        }

        return shuffled
    }

}