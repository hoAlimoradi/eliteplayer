package com.alimoradi.core.interactor.lastfm

import com.alimoradi.core.IEncrypter
import com.alimoradi.core.entity.UserCredentials
import com.alimoradi.core.prefs.AppPreferencesGateway
import javax.inject.Inject

class GetLastFmUserCredentials @Inject constructor(
    private val gateway: AppPreferencesGateway,
    private val lastFmEncrypter: IEncrypter

) {

    fun execute(): UserCredentials {
        return decryptUser(gateway.getLastFmCredentials())
    }

    private fun decryptUser(user: UserCredentials): UserCredentials {
        return UserCredentials(
            lastFmEncrypter.decrypt(user.username),
            lastFmEncrypter.decrypt(user.password)
        )
    }

}