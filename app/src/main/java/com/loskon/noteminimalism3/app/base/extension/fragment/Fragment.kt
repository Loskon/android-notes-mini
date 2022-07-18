package com.loskon.noteminimalism3.app.base.extension.fragment

import android.graphics.Typeface
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.loskon.noteminimalism3.app.base.extension.context.getColorControlHighlightKtx
import com.loskon.noteminimalism3.app.base.extension.context.getColorKtx
import com.loskon.noteminimalism3.app.base.extension.context.getColorPrimaryKtx
import com.loskon.noteminimalism3.app.base.extension.context.getFontKtx

val Fragment.colorPrimary: Int get() = requireContext().getColorPrimaryKtx()

val Fragment.controlHighlight: Int get() = requireContext().getColorControlHighlightKtx()

fun Fragment.getColor(@ColorRes colorId: Int): Int = requireContext().getColorKtx(colorId)

fun Fragment.getFontType(@FontRes fontId: Int) = requireContext().getFontKtx(fontId)

fun Fragment.requireFont(@FontRes fontId: Int): Typeface {
    return getFontType(fontId) ?: throw NullPointerException("Fragment $this could not find the font.")
}

fun Fragment.setFragmentResultListener(requestKey: String, onResult: (Bundle) -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        onResult(bundle)
        bundle.clear()
    }
}