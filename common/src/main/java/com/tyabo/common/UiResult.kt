package com.tyabo.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface UiResult<out T> {
    data class Success<T>(val data: T) : UiResult<T>
    data class Failure(val exception: Throwable? = null) : UiResult<Nothing>
    object Loading : UiResult<Nothing>
}

fun <T> Flow<T>.asUiResult(): Flow<UiResult<T>> {
    return this
        .map<T, UiResult<T>> {
            UiResult.Success(it)
        }
        .onStart { emit(UiResult.Loading) }
        .catch { emit(UiResult.Failure(it)) }
}

fun <T> Result<T>.asUiResult(): UiResult<T> {
    this.onSuccess {
        return UiResult.Success(it)
    }.onFailure {
        return UiResult.Failure(it)
    }
    return UiResult.Loading
}

inline fun <R, T> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return fold(
        onSuccess = { transform(it) },
        onFailure = { Result.failure(it) }
    )
}
