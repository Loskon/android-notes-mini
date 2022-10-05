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
    private val view: View,
    private val anchorView: View
) : BaseSnackbar() {

    private var binding: SnackbarUndoBinding? = null
    private var undoClickListener: ((Note, Boolean) -> Unit)? = null

    fun make(note: Note, isFavorite: Boolean, category: String): NoteListUndoSnackbar {
        binding = SnackbarUndoBinding.inflate(LayoutInflater.from(context))

        val countDownTimer = getConfiguredTimer()
        val animator = getConfiguredAnimator()

        make(view, binding?.root)
        setAnchorView(anchorView)

        binding?.tvProgressSnackbarUndo?.text = context.getString(getMessageStringId(category))
        binding?.btnUndo?.setDebounceClickListener { handleUndoClick(note, isFavorite) }
        setOnDismissedListener { handleOnDismissedListener(countDownTimer, animator) }

        countDownTimer.start()
        animator.start()

        return this
    }

    private fun getConfiguredTimer(): CountDownTimer {
        return object : CountDownTimer(5300, 100) {
            override fun onTick(leftTimeInMilliseconds: Long) {
                val seconds = leftTimeInMilliseconds / 1000
                binding?.tvProgressSnackbarUndo?.text = seconds.toString()
            }

            override fun onFinish() {
                dismiss()
            }
        }
    }

    private fun getConfiguredAnimator(): ObjectAnimator {
        return ObjectAnimator.ofInt(binding?.progressBarSnackbarUndo, "progress", binding?.progressBarSnackbarUndo?.max ?: 0)
            .apply {
                duration = 5000
                interpolator = DecelerateInterpolator()
            }
    }

    private fun getMessageStringId(category: String): Int {
        return if (category == NoteListViewModel.CATEGORY_TRASH1) {
            R.string.sb_undo_note_deleted
        } else {
            R.string.sb_undo_note_added_trash
        }
    }

    private fun handleUndoClick(note: Note, isFavorite: Boolean) {
        undoClickListener?.invoke(note, isFavorite)
        dismiss()
    }

    private fun handleOnDismissedListener(countDownTimer: CountDownTimer, animator: ObjectAnimator) {
        animator.cancel()
        countDownTimer.cancel()
        binding = null
    }

    fun setUndoClickListener(undoClickListener: ((Note, Boolean) -> Unit)?) {
        this.undoClickListener = undoClickListener
    }
}