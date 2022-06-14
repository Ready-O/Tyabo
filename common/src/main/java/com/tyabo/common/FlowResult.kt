package com.tyabo.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface FlowResult<out T> {
    data class Success<T>(val data: T) : FlowResult<T>
    data class Failure(val exception: Throwable? = null) : FlowResult<Nothing>
    object Loading : FlowResult<Nothing>
}

fun <T> Flow<T>.asFlowResult(): Flow<FlowResult<T>> {
    return this
        .map<T, FlowResult<T>> {
            FlowResult.Success(it)
        }
        .onStart { emit(FlowResult.Loading) }
        .catch { emit(FlowResult.Failure(it)) }
}

fun <T> Result<T>.asFlowResult(): FlowResult<T> {
    this.onSuccess {
        return FlowResult.Success(it)
    }.onFailure {
        return FlowResult.Failure(it)
    }
    return FlowResult.Loading
}