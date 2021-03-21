package com.alimoradi.shared

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Suppress("FunctionName")
fun CustomScope(dispatcher: CoroutineDispatcher = Dispatchers.Default): CoroutineScope =
    CoroutineScope(SupervisorJob() + dispatcher)