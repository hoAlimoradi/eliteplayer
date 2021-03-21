package com.alimoradi.servicefloating

import android.content.Context
import dev.olog.core.dagger.ApplicationContext
import dev.olog.core.gateway.OfflineLyricsGateway
import com.alimoradi.offlinelyrics.BaseOfflineLyricsPresenter
import com.alimoradi.offlinelyrics.domain.InsertOfflineLyricsUseCase
import com.alimoradi.offlinelyrics.domain.ObserveOfflineLyricsUseCase
import javax.inject.Inject

class OfflineLyricsContentPresenter @Inject constructor(
    @ApplicationContext context: Context,
    lyricsGateway: OfflineLyricsGateway,
    observeUseCase: ObserveOfflineLyricsUseCase,
    insertUseCase: InsertOfflineLyricsUseCase

) : BaseOfflineLyricsPresenter(
    context,
    lyricsGateway,
    observeUseCase,
    insertUseCase
)