package com.alimoradi.presentation.about

import android.content.res.ColorStateList
import androidx.lifecycle.Lifecycle
import com.alimoradi.presentation.base.adapter.DataBoundViewHolder
import com.alimoradi.presentation.base.adapter.DiffCallbackDisplayableItem
import com.alimoradi.presentation.base.adapter.ObservableAdapter
import com.alimoradi.presentation.base.adapter.setOnClickListener
import com.alimoradi.presentation.model.DisplayableHeader
import com.alimoradi.presentation.model.DisplayableItem
import com.alimoradi.presentation.navigator.NavigatorAbout
import com.alimoradi.sharedandroid.extensions.colorAccent
import kotlinx.android.synthetic.main.item_about.view.*


class AboutFragmentAdapter(
    lifecycle: Lifecycle,
    private val navigator: NavigatorAbout,
    private val presenter: AboutFragmentPresenter

) : ObservableAdapter<DisplayableItem>(
    lifecycle,
    DiffCallbackDisplayableItem
) {

    override fun initViewHolderListeners(viewHolder: DataBoundViewHolder, viewType: Int) {
        viewHolder.setOnClickListener(this) { item, _, _ ->
            when (item.mediaId) {
                AboutFragmentPresenter.HAVOC_ID -> navigator.toHavocPage()
                AboutFragmentPresenter.THIRD_SW_ID -> navigator.toLicensesFragment()
                AboutFragmentPresenter.SPECIAL_THANKS_ID -> navigator.toSpecialThanksFragment()
                AboutFragmentPresenter.RATE_ID -> navigator.toMarket()
                AboutFragmentPresenter.PRIVACY_POLICY -> navigator.toPrivacyPolicy()
                AboutFragmentPresenter.BUY_PRO -> presenter.buyPro()
                AboutFragmentPresenter.COMMUNITY -> navigator.joinCommunity()
                AboutFragmentPresenter.BETA -> navigator.joinBeta()
                AboutFragmentPresenter.CHANGELOG -> navigator.toChangelog()
                AboutFragmentPresenter.GITHUB -> navigator.toGithub()
                AboutFragmentPresenter.TRANSLATION -> navigator.toTranslations()
            }
        }
    }

    override fun bind(holder: DataBoundViewHolder, item: DisplayableItem, position: Int) {
        require(item is DisplayableHeader)
        holder.itemView.apply {
            if (item.mediaId == AboutFragmentPresenter.BUY_PRO) {
                title.setTextColor(ColorStateList.valueOf(context.colorAccent()))
            }
            title.text = item.title
            subtitle.text = item.subtitle   
        }
    }

}