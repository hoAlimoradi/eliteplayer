package com.alimoradi.presentation.pro

import kotlinx.coroutines.flow.Flow

interface IBilling {

    fun observeBillingsState(): Flow<BillingState>
    fun getBillingsState(): BillingState
    fun purchasePremium()

}

