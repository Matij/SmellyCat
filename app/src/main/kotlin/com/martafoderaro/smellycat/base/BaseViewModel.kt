package com.martafoderaro.smellycat.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive

abstract class BaseViewModel<T : UiState, in E : UiEvent> : ViewModel() {
    abstract val state: Flow<T>

    fun CoroutineScope.launchPeriodicAsync(repeatMillis: Long, action: () -> Unit) = this.async {
        while (isActive) {
            action()
            delay(repeatMillis)
        }
    }
}