package com.alimoradi.presentation.about

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alimoradi.core.MediaId
import com.alimoradi.presentation.BuildConfig
import com.alimoradi.presentation.R
import com.alimoradi.presentation.model.DisplayableHeader
import com.alimoradi.presentation.model.DisplayableItem
import com.alimoradi.presentation.pro.IBilling
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class AboutFragmentPresenter(
    context: Context,
    private val billing: IBilling
) : CoroutineScope by MainScope() {

    companion object {
        @JvmStatic
        val HAVOC_ID = MediaId.headerId("havoc id")
        @JvmStatic
        val AUTHOR_ID = MediaId.headerId("author id")
        @JvmStatic
        val THIRD_SW_ID = MediaId.headerId("third sw")
        @JvmStatic
        val COMMUNITY = MediaId.headerId("community")
        @JvmStatic
        val BETA = MediaId.headerId("beta")
        @JvmStatic
        val SPECIAL_THANKS_ID = MediaId.headerId("special thanks to")
        @JvmStatic
        val TRANSLATION = MediaId.headerId("Translation")
        @JvmStatic
        val RATE_ID = MediaId.headerId("rate")
        @JvmStatic
        val PRIVACY_POLICY = MediaId.headerId("privacy policy")
        @JvmStatic
        val BUY_PRO = MediaId.headerId("pro")
        @JvmStatic
        val CHANGELOG = MediaId.headerId("changelog")
        @JvmStatic
        val GITHUB = MediaId.headerId("github")
    }


    private val data = listOf(
        DisplayableHeader(
            type = R.layout.item_about_promotion,
            mediaId = HAVOC_ID,
            title = context.getString(R.string.about_havoc),
            subtitle = context.getString(R.string.about_translations_description)
        ),
        DisplayableHeader(
            type = R.layout.item_about,
            mediaId = AUTHOR_ID,
            title = context.getString(R.string.about_author),
            subtitle = "alimoradi"
        ),
        DisplayableHeader(
            type = R.layout.item_about,
            mediaId = MediaId.headerId("version id"),
            title = context.getString(R.string.about_version),
            subtitle = BuildConfig.VERSION_NAME
        ),


        DisplayableHeader(
            type = R.layout.item_about,
            mediaId = SPECIAL_THANKS_ID,
            title = context.getString(R.string.about_special_thanks_to),
            subtitle = context.getString(R.string.about_special_thanks_to_description)
        )



    )


    private val dataLiveData = MutableLiveData<List<DisplayableItem>>()

    init {

        dataLiveData.value = data
    }

    fun onCleared() {
        cancel()
    }

    fun observeData(): LiveData<List<DisplayableItem>> = dataLiveData

    fun buyPro() {
        if (!billing.getBillingsState().isPremiumStrict()) {
            billing.purchasePremium()
        }
    }
}