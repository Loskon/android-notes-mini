package com.loskon.noteminimalism3.base.extension.bundle

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

@Suppress("DEPRECATION")
inline fun <reified T: Parcelable> Bundle.getParcelableKtx(key: String): T? {
   return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    }else{
        getParcelable(key)
    }
}

@Suppress("DEPRECATION")
inline fun <reified T: Serializable> Bundle.getSerializableKtx(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, T::class.java)
    }else{
        getSerializable(key) as? T
    }
}