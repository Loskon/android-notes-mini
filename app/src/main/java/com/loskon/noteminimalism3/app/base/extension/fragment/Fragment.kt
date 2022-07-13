package com.loskon.noteminimalism3.app.base.extension.fragment

import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.app.base.extension.context.getColorControlHighlightKtx
import com.loskon.noteminimalism3.app.base.extension.context.getColorKtx
import com.loskon.noteminimalism3.app.base.extension.context.getColorPrimaryKtx
import com.loskon.noteminimalism3.app.base.extension.context.getFontKtx

val Fragment.colorPrimary: Int get() = requireContext().getColorPrimaryKtx()

val Fragment.controlHighlight: Int get() = requireContext().getColorControlHighlightKtx()

fun Fragment.getColor(@ColorRes colorId: Int): Int = requireContext().getColorKtx(colorId)

fun Fragment.getFont(@FontRes fontId: Int) = requireContext().getFontKtx(fontId)