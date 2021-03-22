package com.alimoradi.core.interactor

import com.alimoradi.core.IEncrypter
import com.alimoradi.core.entity.UserCredentials
import com.alimoradi.core.interactor.base.FlowUseCase
import com.alimoradi.core.prefs.AppPreferencesGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveLastFmUserCredentials @Inject constructor(
    private val gateway: AppPreferencesGateway,
    private val lastFmEncrypter: IEncrypter

) : FlowUseCase<UserCredentials>() {

    override fun buildUseCase(): Flow<UserCredentials> {
        return gateway.observeLastFmCredentials()
            .map { decryptUser(it) }
    }

    private fun decryptUser(user: UserCredentials): UserCredentials {
        return UserCredentials(
            lastFmEncrypter.decrypt(user.username),
            lastFmEncrypter.decrypt(user.password)
        )
    }

}