package com.loskon.noteminimalism3.base.extension.flow

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Flow<T>.observe(lifecycle: LifecycleOwner, noinline block: suspend (T) -> Unit) {
    lifecycle.lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect(block)
        }
    }
}