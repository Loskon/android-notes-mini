package com.loskon.noteminimalism3.backup.update

import com.firebase.ui.auth.IdpResponse

/**
 * Интерфейс
 */

interface GoogleResultInterface {
    fun onRequestGoogleResult(isGranted: Boolean, response: IdpResponse?)
}