package com.loskon.noteminimalism3.app.base.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseViewModel : ViewModel() {

    private val errorState = MutableStateFlow<Throwable?>(null)
    open val errorStateFlow get() = errorState.asStateFlow()

    protected fun launchErrorJob(
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        error: MutableStateFlow<Throwable?>? = errorState,
        errorBlock: ((Throwable) -> Unit)? = null,
        block: suspend () -> Unit
    ): Job {
        return viewModelScope.launch(dispatcher + getExceptionHandler(error, errorBlock)) {
            try {
                error?.tryEmit(null)
                block()
            } finally {
                // Nothing
            }
        }
    }

    private fun getExceptionHandler(
        error: MutableStateFlow<Throwable?>?,
        errorBlock: ((Throwable) -> Unit)? = null
    ): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable)
            errorBlock?.invoke(throwable)
            error?.tryEmit(throwable)
        }
    }
}