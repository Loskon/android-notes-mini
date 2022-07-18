package com.loskon.noteminimalism3.app.base.extension.activity

import android.app.Activity
import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.fragment.app.FragmentActivity

@Suppress("DEPRECATION")
fun Activity.installTaskDescriptionColor(@ColorRes color: Int) {
    setTaskDescription(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ActivityManager.TaskDescription(null, 0, getColor(color))
        } else {
            ActivityManager.TaskDescription(null, null, getColor(color))
        }
    )
}

fun FragmentActivity.setFragmentResultListener(requestKey: String, block: (bundle: Bundle) -> Unit) {
    supportFragmentManager.setFragmentResultListener(requestKey, this) { _, bundle ->
        block (bundle)
    }
}