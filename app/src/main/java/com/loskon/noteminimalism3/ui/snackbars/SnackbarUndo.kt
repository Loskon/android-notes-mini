package com.loskon.noteminimalism3.ui.snackbars

import android.animation.ObjectAnimator
import android.os.CountDownTimer
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.command.ShortsCommand
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.ui.activities.ListActivity
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Кастомный Snackbar с таймером
 */

class SnackbarUndo(
    private val activity: ListActivity,
    private val shortsCommand: ShortsCommand
) {

    private val fab: FloatingActionButton = activity.getFab
    private val coordLayout: CoordinatorLayout = activity.getCoordLayout

    private var snackbar: Snackbar? = null
    private var countDownTimer: CountDownTimer? = null

    private lateinit var btnSnackbar: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress: TextView
    private lateinit var tvTitle: TextView

    private var notesCategory: String = ""

    fun show(note: Note, isFavorite: Boolean) {
        countDownTimer?.cancel()
        snackbar?.dismiss()

        notesCategory = activity.getNotesCategory()

        snackbar = Snackbar.make(coordLayout, "", Snackbar.LENGTH_INDEFINITE)
        snackbar?.anchorView = fab

        val layout = snackbar?.view as SnackbarLayout
        val view: View = View.inflate(activity, R.layout.snackbar_undo, null)
        view.setOnClickListener { snackbar?.dismiss() }

        initViews(view)
        configViews()
        clickingSnackbarButton(note, isFavorite)
        setupAnimation()
        installTimer()

        layout.addView(view, 0)
        snackbar?.show()
    }

    private fun initViews(snackView: View) {
        btnSnackbar = snackView.findViewById(R.id.btn_snackbar_undo)
        progressBar = snackView.findViewById(R.id.progress_bar_snackbar_undo)
        tvProgress = snackView.findViewById(R.id.tv_snackbar_undo_progress)
        tvTitle = snackView.findViewById(R.id.tv_snackbar_undo_text)
    }

    private fun configViews() {
        progressBar.progress = 0
        progressBar.max = 10000
        tvTitle.text = activity.getString(getMessage())
    }

    private fun getMessage(): Int = if (notesCategory == CATEGORY_TRASH) {
        R.string.sb_undo_note_deleted
    } else {
        R.string.sb_undo_note_added_trash
    }

    private fun clickingSnackbarButton(note: Note, isFavorite: Boolean) {
        btnSnackbar.setOnSingleClickListener {
            if (notesCategory == CATEGORY_TRASH) {
                shortsCommand.insert(note)
            } else {
                note.isFavorite = isFavorite
                note.isDelete = false
                shortsCommand.update(note)
            }

            activity.updateListNotes()
            countDownTimer?.cancel()
            snackbar?.dismiss()
        }
    }

    private fun setupAnimation() {
        val animation = ObjectAnimator.ofInt(progressBar, "progress", 10000)
        animation.duration = 4900 // 5 second
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }

    private fun installTimer() {
        countDownTimer = object : CountDownTimer(5000, 100) {
            override fun onTick(leftTimeInMilliseconds: Long) {
                val seconds: Long = leftTimeInMilliseconds / 1000
                tvProgress.text = seconds.toString()
            }

            override fun onFinish() {
                snackbar?.dismiss()
            }
        }.start()
    }

    fun dismiss() {
        snackbar?.dismiss()
        countDownTimer?.cancel()
    }
}