@file:Suppress("DEPRECATION")

package com.alimoradi.presentation.prefs.lastfm

import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.umass.lastfm.Authenticator
import com.alimoradi.core.entity.UserCredentials
import com.alimoradi.core.interactor.lastfm.GetLastFmUserCredentials
import com.alimoradi.core.interactor.lastfm.UpdateLastFmUserCredentials
import com.alimoradi.presentation.BuildConfig
import com.alimoradi.presentation.R
import com.alimoradi.presentation.base.BaseDialogFragment
import com.alimoradi.sharedandroid.extensions.ctx
import com.alimoradi.sharedandroid.extensions.toast
import kotlinx.coroutines.*
import javax.inject.Inject

class LastFmCredentialsFragment : BaseDialogFragment(), CoroutineScope by MainScope() {

    companion object {
        const val TAG = "LastFmCredentialsFragment"

        @JvmStatic
        fun newInstance(): LastFmCredentialsFragment {
            return LastFmCredentialsFragment()
        }
    }

    @Inject
    lateinit var getLastFmUserCredentials: GetLastFmUserCredentials
    @Inject
    lateinit var updateLastFmUserCredentials: UpdateLastFmUserCredentials

    private var loader: ProgressDialog? = null

    private var job: Job? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(activity!!)
        val view: View = inflater.inflate(R.layout.fragment_credentials, null, false)

        val builder = MaterialAlertDialogBuilder(ctx)
            .setTitle(R.string.prefs_last_fm_credentials_title)
            .setMessage(R.string.prefs_last_fm_credentials_message)
            .setView(view)
            .setPositiveButton(R.string.credentials_button_positive, null)
            .setNegativeButton(R.string.credentials_button_negative, null)

        val userName = view.findViewById<EditText>(R.id.username)
        val password = view.findViewById<EditText>(R.id.password)

        val credentials = getLastFmUserCredentials.execute()
        userName.setText(credentials.username)
        password.setText(credentials.password)

        val dialog = builder.show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            job = launch {

                val user = UserCredentials(
                    userName.text.toString(),
                    password.text.toString()
                )
                showLoader()
                try {
                    if (tryAuthenticate(user)) {
                        onSuccess(user)
                    } else {
                        onFail()
                    }
                } catch (ex: Throwable) {
                    onFail()
                } finally {
                    loader?.dismiss()
                }
            }

        }

        return dialog
    }

    private fun showLoader() {
        loader = ProgressDialog.show(context, "", "Authenticating", true).apply {
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            setOnCancelListener {
                setOnCancelListener {
                    job?.cancel()
                    loader = null
                }
            }
        }
    }

    private suspend fun tryAuthenticate(user: UserCredentials): Boolean =
        withContext(Dispatchers.IO) {
            Authenticator.getMobileSession(
                user.username,
                user.password,
                BuildConfig.LAST_FM_KEY,
                BuildConfig.LAST_FM_SECRET
            ) != null

        }

    private fun onSuccess(user: UserCredentials) {
        updateLastFmUserCredentials(user)
        ctx.toast("Success")
        dismiss()
    }

    private fun onFail() {
        ctx.toast("Failed")
    }

}