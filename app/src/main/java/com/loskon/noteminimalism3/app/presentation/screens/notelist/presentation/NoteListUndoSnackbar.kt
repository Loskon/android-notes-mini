package com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation

import android.animation.ObjectAnimator
import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.widget.snackbar.BaseCustomSnackbar
import com.loskon.noteminimalism3.databinding.SnackbarUndoBinding

class NoteListUndoSnackbar(private var context: Context) {

    val binding = SnackbarUndoBinding.inflate(LayoutInflater.from(context))

    private var snackbar: BaseCustomSnackbar? = null
    private var countDownTimer: CountDownTimer? = null
    private var anim: ObjectAnimator? = null

    init {
        binding.btnSnackbarUndo.setDebounceClickListener {  }
        setupAnimation()
        installTimer()
    }

    fun show(view: View, anchorView: View) {
        anim?.cancel()
        countDownTimer?.cancel()

        anim?.start()
        countDownTimer?.start()

        snackbar = BaseCustomSnackbar().make(view, binding.root).setAnchorView(anchorView)
        snackbar?.show()
    }

    private fun setupAnimation() {
        anim = ObjectAnimator.ofInt(binding.progressBarSnackbarUndo, "progress", binding.progressBarSnackbarUndo.max)
        anim?.duration = 5000
        anim?.interpolator = DecelerateInterpolator()
    }

    private fun installTimer() {
        countDownTimer = object : CountDownTimer(5000, 100) {
            override fun onTick(leftTimeInMilliseconds: Long) {
                val seconds: Long = leftTimeInMilliseconds / 1000
                binding.tvSnackbarUndoProgress.text = seconds.toString()
            }

            override fun onFinish() {
                snackbar?.dismiss()
            }
        }
    }

    fun dismiss() {
        anim?.cancel()
        countDownTimer?.cancel()
        snackbar?.dismiss()
    }
}