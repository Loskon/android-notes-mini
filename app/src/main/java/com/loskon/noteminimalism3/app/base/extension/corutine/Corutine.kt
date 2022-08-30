package com.loskon.noteminimalism3.app.base.extension.corutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun CoroutineScope.launchDelay(time: Int, block: () -> Unit) {
    launch {
        delay(time.toLong())
        block()
    }
}