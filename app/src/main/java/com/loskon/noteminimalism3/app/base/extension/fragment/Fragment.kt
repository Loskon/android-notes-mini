package com.loskon.noteminimalism3.app.base.extension.fragment

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.addCallback
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.loskon.noteminimalism3.app.base.extension.context.getColorControlHighlightKtx
import com.loskon.noteminimalism3.app.base.extension.context.getColorKtx
import com.loskon.noteminimalism3.app.base.extension.context.getColorPrimaryKtx
import com.loskon.noteminimalism3.app.base.extension.context.getDimenKtx
import com.loskon.noteminimalism3.app.base.extension.context.getDrawableKtx
import com.loskon.noteminimalism3.app.base.extension.context.getFontKtx
import com.loskon.noteminimalism3.app.base.extension.context.showToast

val Fragment.colorPrimary: Int get() = requireContext().getColorPrimaryKtx()

val Fragment.controlHighlight: Int get() = requireContext().getColorControlHighlightKtx()

fun Fragment.getColor(@ColorRes colorId: Int): Int = requireContext().getColorKtx(colorId)

fun Fragment.getFont(@FontRes fontId: Int) = requireContext().getFontKtx(fontId)

fun Fragment.getDrawable(@DrawableRes drawableId: Int) = requireContext().getDrawableKtx(drawableId)

fun Fragment.getDimen(@DimenRes dimenId: Int): Int = requireContext().getDimenKtx(dimenId)

fun Fragment.requireFont(@FontRes fontId: Int): Typeface {
    return getFont(fontId) ?: throw NullPointerException("Fragment $this could not find the font.")
}

fun Fragment.requireDrawable(@DrawableRes drawableId: Int): Drawable {
    return getDrawable(drawableId) ?: throw NullPointerException("Fragment $this could not find the drawable.")
}

fun Fragment.setFragmentResultListener(requestKey: String, onResult: (Bundle) -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        onResult(bundle)
        bundle.clear()
    }
}

fun Fragment.setFragmentClick(requestKey: String) {
    setFragmentResult(requestKey, Bundle())
}

fun Fragment.setFragmentClickListener(requestKey: String, onResult: () -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        onResult()
        bundle.clear()
    }
}

@SuppressLint("ClickableViewAccessibility")
fun Fragment.setFragmentScreenOnTouchListener(onClick: () -> Unit) {
    requireView().setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            onClick()
        }
        true
    }
}

fun Fragment.setOnBackPressedListener(onClick: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(this) {
        onClick()
    }
}

inline fun <T : Fragment> T.putArgs(argsBuilder: Bundle.() -> Unit): T = apply {
    arguments = Bundle().apply(argsBuilder)
}

fun Fragment.showToast(message: String) {
    requireContext().showToast(message)
}

fun Fragment.showToast(@StringRes stringId: Int) {
    requireContext().showToast(getString(stringId))
}