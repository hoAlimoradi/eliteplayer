package com.alimoradi.presentation.splash

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Priority
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.alimoradi.imageprovider.CoverUtils
import com.alimoradi.imageprovider.GlideApp
import com.alimoradi.presentation.R
import com.alimoradi.presentation.widgets.StoppingViewPager
import com.alimoradi.presentation.widgets.swipeableview.SwipeableView
import com.alimoradi.sharedandroid.extensions.asLiveData
import com.alimoradi.sharedandroid.extensions.ctx
import com.alimoradi.sharedandroid.extensions.subscribe
import kotlinx.android.synthetic.main.fragment_splash_tutorial.*

class SplashTutorialFragment : Fragment(),
    SwipeableView.SwipeListener {

    private var progressive = 0

    private lateinit var viewPager : StoppingViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash_tutorial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = parentFragment!!.view!!.findViewById(R.id.viewPager)

        swipeableView.isTouching()
            .asLiveData()
            .subscribe(this) {
                viewPager.isSwipeEnabled = !it
            }

        loadPhoneImage(view)
        loadImage(progressive)
    }

    override fun onResume() {
        super.onResume()
        swipeableView.setOnSwipeListener(this)
    }

    override fun onPause() {
        super.onPause()
        swipeableView.setOnSwipeListener(null)
    }

    override fun onSwipedLeft() {
        loadNextImage()
        setActivated(true)
    }

    override fun onSwipedRight() {
        loadPreviousImage()
        setActivated(true)
    }

    override fun onClick() {
        val newState = !cover.isActivated
        setActivated(newState)
    }

    override fun onLeftEdgeClick() {
        loadPreviousImage()
        setActivated(true)
    }

    override fun onRightEdgeClick() {
        loadNextImage()
        setActivated(true)
    }

    private fun setActivated(activated: Boolean){
        coverWrapper.isActivated = activated
        nowPlaying.isActivated = activated
    }

    private fun loadNextImage(){
        loadImage(++progressive)
    }

    private fun loadPreviousImage(){
        loadImage(--progressive)
    }

    private fun loadPhoneImage(view: View){
        GlideApp.with(requireContext())
                .asBitmap()
                .load(R.drawable.phone_black)
                .priority(Priority.IMMEDIATE)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        view.findViewById<ImageView>(R.id.phoneImage).setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
    }

    private fun loadImage(position: Int){
        GlideApp.with(ctx).clear(cover)

        GlideApp.with(ctx)
                .load(Uri.EMPTY)
                .centerCrop()
                .placeholder(CoverUtils.getGradient(ctx, position))
                .into(cover)
    }
}