package com.alimoradi.core.interactor.lastfm

import com.alimoradi.core.IEncrypter
import com.alimoradi.core.entity.UserCredentials
import com.alimoradi.core.prefs.AppPreferencesGateway
import javax.inject.Inject

class UpdateLastFmUserCredentials @Inject constructor(
    private val gateway: AppPreferencesGateway,
    private val lastFmEncrypter: IEncrypter

) {

    operator fun invoke(param: UserCredentials){
        val user = encryptUser(param)
        gateway.setLastFmCredentials(user)
    }

    private fun encryptUser(user: UserCredentials): UserCredentials {
        return UserCredentials(
            lastFmEncrypter.encrypt(user.username),
            lastFmEncrypter.encrypt(user.password)
        )
    }

}