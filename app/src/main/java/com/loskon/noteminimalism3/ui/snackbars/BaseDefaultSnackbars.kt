package com.loskon.noteminimalism3.ui.snackbars

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.ContentViewCallback
import com.google.android.material.snackbar.Snackbar

/**
 * Основа для стандартного Snackbar с вставкой кастомной компоновки
 */

class BaseDefaultSnackbar constructor(
    parent: ViewGroup, content: View,
    viewCallback: CustomContentViewCallback
) : BaseTransientBottomBar<BaseDefaultSnackbar>(parent, content, viewCallback) {
    companion object {

        fun make(
            @LayoutRes layoutId: Int,
            parent: ViewGroup,
            anchorView: View,
            isiIndefinite: Boolean
        ): BaseDefaultSnackbar {

            val content: View = View.inflate(parent.context, layoutId, null)

            val viewCallback = CustomContentViewCallback()
            val snackbar = BaseDefaultSnackbar(parent, content, viewCallback)

            snackbar.anchorView = anchorView
            snackbar.setSnackDuration(isiIndefinite)
            snackbar.getView().setGravityBottom(parent)
            snackbar.getView().setOnClickListener { snackbar.dismiss() }

            return snackbar
        }
    }
}

private fun BaseDefaultSnackbar.setSnackDuration(isiIndefinite: Boolean) {
    duration = if (isiIndefinite) {
        Snackbar.LENGTH_INDEFINITE
    } else {
        Snackbar.LENGTH_LONG
    }
}

private fun View.setGravityBottom(parent: ViewGroup) {
    val params: ViewGroup.LayoutParams = this.layoutParams

    when (params) {
        is CoordinatorLayout.LayoutParams -> {
            params.gravity = Gravity.BOTTOM
        }

        is FrameLayout.LayoutParams -> {
            params.gravity = Gravity.BOTTOM
        }

        is ConstraintLayout.LayoutParams -> {
            params.bottomToBottom = parent.id
        }

        else -> {
            throw ClassCastException("$params cannot be to anything")
        }
    }

    this.layoutParams = params
}

// Интерфейс, определяющий анимацию основного содержимого Snackbar
class CustomContentViewCallback : ContentViewCallback {

    override fun animateContentIn(delay: Int, duration: Int) {
    }

    override fun animateContentOut(delay: Int, duration: Int) {
    }
}

