package com.loskon.noteminimalism3.base.extension.dialogfragment

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment.onlyShow(fragmentManager: FragmentManager, tag: String) {
    if (fragmentManager.findFragmentByTag(tag) == null) {
        show(fragmentManager, tag)
    }
}

fun DialogFragment.dismissShowing() {
    if (requireDialog().isShowing) dismiss()
}

fun DialogFragment.show(manager: FragmentManager) = show(manager, null)