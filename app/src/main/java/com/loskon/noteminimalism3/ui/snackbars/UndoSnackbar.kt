package com.loskon.noteminimalism3.ui.snackbars

import android.animation.ObjectAnimator
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.commands.CommandCenter
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener

/**
 * Snackbar с таймером для восстановления удаленной заметки
 */

class UndoSnackbar(
    private val activity: MainActivity,
    private val commandCenter: CommandCenter,
    private val layout: ViewGroup,
    private val fab: View
) {

    private var snackbar: BaseDefaultSnackbar? = null
    private var countDownTimer: CountDownTimer? = null
    private var anim: ObjectAnimator? = null

    fun show(note: Note, hasFavStatus: Boolean, category: String) {
        anim?.cancel()
        countDownTimer?.cancel()

        snackbar = BaseDefaultSnackbar.make(R.layout.snackbar_undo, layout, fab, true)
        snackbar?.settingSnackbarView(note, hasFavStatus, category)

        anim?.start()
        countDownTimer?.start()
        snackbar?.show()
    }

    private fun BaseDefaultSnackbar?.settingSnackbarView(
        note: Note,
        hasFavStatus: Boolean,
        category: String
    ) {
        val view: View? = this?.view

        val btnSnackbar: Button? = view?.findViewById(R.id.btn_snackbar_undo)
        val progressBar: ProgressBar? = view?.findViewById(R.id.progress_bar_snackbar_undo)
        val tvProgress: TextView? = view?.findViewById(R.id.tv_snackbar_undo_progress)
        val tvTitle: TextView? = view?.findViewById(R.id.tv_snackbar_undo_text)

        btnSnackbar?.setDebounceClickListener { performRestoreNote(note, hasFavStatus, category) }
        progressBar?.setupAnimation()
        tvProgress?.installTimer()
        tvTitle?.messageText(category)
    }

    private fun performRestoreNote(note: Note, hasFavStatus: Boolean, category: String) {
        if (category == CATEGORY_TRASH) {
            commandCenter.insert(note)
        } else {
            commandCenter.resetFromTrash(note, hasFavStatus)
        }

        activity.updateNoteList()

        dismiss()
    }

    private fun ProgressBar.setupAnimation() {
        progress = 0
        max = 10000

        anim = ObjectAnimator.ofInt(this, "progress", max)
        anim?.duration = 4500 // 5 second
        anim?.interpolator = DecelerateInterpolator()
    }

    private fun TextView.installTimer() {
        countDownTimer = object : CountDownTimer(5000, 100) {
            override fun onTick(leftTimeInMilliseconds: Long) {
                val seconds: Long = leftTimeInMilliseconds / 1000
                text = seconds.toString()
            }

            override fun onFinish() {
                snackbar?.dismiss()
            }
        }
    }

    private fun TextView.messageText(category: String) {
        val stringId: Int = if (category == CATEGORY_TRASH) {
            R.string.sb_undo_note_deleted
        } else {
            R.string.sb_undo_note_added_trash
        }

        text = activity.getString(stringId)
    }

    fun dismiss() {
        anim?.cancel()
        countDownTimer?.cancel()
        snackbar?.dismiss()
    }
}