package com.alimoradi.presentation.pro

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.CallSuper
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.alimoradi.presentation.R
import com.alimoradi.sharedandroid.extensions.toast
import java.lang.ref.WeakReference

internal abstract class BillingConnection(
    protected val activity: WeakReference<FragmentActivity>
) : PurchasesUpdatedListener, DefaultLifecycleObserver {

    private var isConnected = false

    protected val billingClient: BillingClient = BillingClient.newBuilder(activity.get()!!)
        .setListener(this)
        .build()

    protected fun doOnConnected(func: (() -> Unit)?) {
        if (isConnected) {
            func?.invoke()
            return
        }

        billingClient.startConnection(object : BillingClientStateListener {
            @SuppressLint("SwitchIntDef")
            override fun onBillingSetupFinished(responseCode: Int) {
                Log.v("Billing", "billing connection response code=${responseCode}")
                when (responseCode) {
                    BillingClient.BillingResponse.SERVICE_TIMEOUT -> {
                        activity.get()?.toast(R.string.network_timeout)
                    }
                    BillingClient.BillingResponse.SERVICE_UNAVAILABLE -> {
                        activity.get()?.toast(R.string.common_no_internet)
                    }
                }

                isConnected = responseCode == BillingClient.BillingResponse.OK
                if (isConnected) {
                    func?.invoke()
                }
            }

            override fun onBillingServiceDisconnected() {
                isConnected = false
            }
        })
    }

    @CallSuper
    override fun onDestroy(owner: LifecycleOwner) {
        if (billingClient.isReady) {
            billingClient.endConnection()
        }
    }

}