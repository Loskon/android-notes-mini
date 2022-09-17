package com.loskon.noteminimalism3.app.screens.notelist.presentation

import android.animation.ObjectAnimator
import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.widget.snackbar.BaseSnackbar
import com.loskon.noteminimalism3.databinding.SnackbarUndoBinding
import com.loskon.noteminimalism3.model.Note

class NoteListUndoSnackbar(
    private val context: Context,
    view: View,
    anchorView: View
) {

    private val binding = SnackbarUndoBinding.inflate(LayoutInflater.from(context))
    private val snackbar = BaseSnackbar().make(view, binding.root).setAnchorView(anchorView)

    private var countDownTimer: CountDownTimer? = null
    private var anim: ObjectAnimator? = null

    private var onCancelClickListener: ((Note, Boolean) -> Unit)? = null

    init {
        setupAnimation()
        installTimer()
    }

    private fun setupAnimation() {
        anim = ObjectAnimator.ofInt(binding.progressBarSnackbarUndo, "progress", binding.progressBarSnackbarUndo.max)
        anim?.duration = 5000
        anim?.interpolator = DecelerateInterpolator()
    }

    private fun installTimer() {
        countDownTimer = object : CountDownTimer(5300, 100) {
            override fun onTick(leftTimeInMilliseconds: Long) {
                val seconds = leftTimeInMilliseconds / 1000
                binding.tvProgressSnackbarUndo.text = seconds.toString()
            }

            override fun onFinish() {
                snackbar.dismiss()
            }
        }
    }

    fun show(note: Note, isFavorite: Boolean, category: String) {
        val stringId = getMessageStringId(category)

        binding.tvProgressSnackbarUndo.text = context.getString(stringId)
        binding.btnUndo.setDebounceClickListener { handleUndoClick(note, isFavorite) }

        anim?.cancel()
        countDownTimer?.cancel()

        anim?.start()
        countDownTimer?.start()

        snackbar.show()
    }

    private fun handleUndoClick(note: Note, isFavorite: Boolean) {
        onCancelClickListener?.invoke(note, isFavorite)
        dismiss()
    }

    private fun getMessageStringId(category: String): Int {
        return if (category == NoteListViewModel.CATEGORY_TRASH1) {
            R.string.sb_undo_note_deleted
        } else {
            R.string.sb_undo_note_added_trash
        }
    }

    fun setOnUndoClickListener(onCancelClickListener: ((Note, Boolean) -> Unit)?) {
        this.onCancelClickListener = onCancelClickListener
    }

    fun dismiss() {
        anim?.cancel()
        countDownTimer?.cancel()
        snackbar.dismiss()
    }
}