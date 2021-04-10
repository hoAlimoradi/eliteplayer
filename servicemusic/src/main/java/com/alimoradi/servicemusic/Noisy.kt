package com.alimoradi.servicemusic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.alimoradi.injection.dagger.PerService
import com.alimoradi.injection.dagger.ServiceContext
import com.alimoradi.servicemusic.EventDispatcher.Event
import javax.inject.Inject

@PerService
internal class Noisy @Inject constructor(
    @ServiceContext private val context: Context,
    private val eventDispatcher: EventDispatcher

) : DefaultLifecycleObserver {

    companion object {
        @JvmStatic
        private val TAG = "SM:${Noisy::class.java.simpleName}"
    }

    private val noisyFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)

    private var registered: Boolean = false

    override fun onDestroy(owner: LifecycleOwner) {
        unregister()
    }

    fun register() {
        if (registered){
            Log.w(TAG, "trying to re-register")
            return
        }
        Log.v(TAG, "register")
        context.registerReceiver(receiver, noisyFilter)
        registered = true
    }

    fun unregister() {
        if (!registered) {
            Log.w(TAG, "trying to unregister but never registered")
            return
        }

        Log.v(TAG, "unregister")
        context.unregisterReceiver(receiver)
        registered = false
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                Log.v(TAG, "on receiver noisy broadcast")
                eventDispatcher.dispatchEvent(Event.PLAY_PAUSE)
            }

        }
    }

}
