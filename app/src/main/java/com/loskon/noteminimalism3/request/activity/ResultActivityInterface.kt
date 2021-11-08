package com.loskon.noteminimalism3.request.activity

import android.net.Uri

interface ResultActivityInterface {
    fun onRequestActivityResult(isGranted: Boolean, requestCode: Int, data: Uri?)
}