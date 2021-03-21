package com.alimoradi.presentation.offlinelyrics

import android.content.Context
import dev.olog.core.dagger.ApplicationContext
import dev.olog.core.gateway.OfflineLyricsGateway
import dev.olog.core.prefs.TutorialPreferenceGateway
import com.alimoradi.intents.AppConstants
import com.alimoradi.offlinelyrics.BaseOfflineLyricsPresenter
import com.alimoradi.offlinelyrics.domain.InsertOfflineLyricsUseCase
import com.alimoradi.offlinelyrics.domain.ObserveOfflineLyricsUseCase
import javax.inject.Inject

class OfflineLyricsFragmentPresenter @Inject constructor(
    @ApplicationContext context: Context,
    observeUseCase: ObserveOfflineLyricsUseCase,
    insertUseCase: InsertOfflineLyricsUseCase,
    private val tutorialPreferenceUseCase: TutorialPreferenceGateway,
    lyricsGateway: OfflineLyricsGateway

) : BaseOfflineLyricsPresenter(context, lyricsGateway, observeUseCase, insertUseCase) {

    private var currentTitle: String = ""
    private var currentArtist: String = ""

    fun updateCurrentMetadata(title: String, artist: String) {
        this.currentTitle = title
        this.currentArtist = artist
    }

    fun getInfoMetadata(): String {
        var result = currentTitle
        if (currentArtist != AppConstants.UNKNOWN) {
            result += " $currentArtist"
        }
        result += " lyrics"
        return result
    }

    fun showAddLyricsIfNeverShown(): Boolean {
        return tutorialPreferenceUseCase.lyricsTutorial()
    }

}