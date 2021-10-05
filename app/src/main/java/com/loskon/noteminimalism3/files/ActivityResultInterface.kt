package com.loskon.noteminimalism3.files

import android.net.Uri

/**
 * Интерфейс
 */

interface ActivityResultInterface {
    fun onRequestActivityResult(isGranted: Boolean, requestCode: Int, data: Uri?)
}