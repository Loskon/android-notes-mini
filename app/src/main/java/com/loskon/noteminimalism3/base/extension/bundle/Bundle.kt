package com.loskon.noteminimalism3.base.extension.bundle

import android.os.Build
import android.os.Bundle

@Suppress("DEPRECATION")
inline fun <reified T: Any> Bundle.getParcelableKtx(key: String): T? {
   return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    }else{
        getParcelable(key)
    }
}