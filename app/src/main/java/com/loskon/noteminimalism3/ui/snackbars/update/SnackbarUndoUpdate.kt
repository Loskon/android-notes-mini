package com.loskon.noteminimalism3.ui.snackbars.update

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
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.ui.activities.update.MainActivityUpdate
import com.loskon.noteminimalism3.viewmodel.AppShortsCommand

/**
 * Кастомный Snackbar с таймером
 */

class SnackbarUndoUpdate(
    private val activity: MainActivityUpdate,
    private val shortsCommand: AppShortsCommand
) {

    private val fab: FloatingActionButton = activity.getFab
    private val coordLayout: CoordinatorLayout = activity.getCoordLayout

    private var snackbar: Snackbar? = null
    private var countDownTimer: CountDownTimer? = null

    private lateinit var btnSnackbar: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress: TextView
    private lateinit var tvTitle: TextView

    fun show(note: Note2, isFavorite: Boolean, category: String) {
        countDownTimer?.cancel()
        snackbar?.dismiss()

        snackbar = Snackbar.make(coordLayout, "", Snackbar.LENGTH_INDEFINITE)
        snackbar?.anchorView = fab

        val layout = snackbar?.view as SnackbarLayout
        val view: View = View.inflate(activity, R.layout.snackbar_swipe, null)

        initViews(view)
        configViews(category)
        clickingSnackbarButton(note, isFavorite, category)
        setAnimation()
        setTimer()

        layout.addView(view, 0)
        snackbar?.show()
    }

    private fun initViews(snackView: View) {
        btnSnackbar = snackView.findViewById(R.id.snackbar_btn)
        progressBar = snackView.findViewById(R.id.snackbar_progress_bar)
        tvProgress = snackView.findViewById(R.id.snackbar_text_progress)
        tvTitle = snackView.findViewById(R.id.snackbar_text_title)
    }

    private fun configViews(category: String) {
        btnSnackbar.setTextColor(MyColor.getMyColor(activity))
        progressBar.progress = 0
        progressBar.max = 10000
        tvTitle.text = activity.getString(getMessage(category))
    }

    private fun getMessage(category: String): Int = if (category == CATEGORY_TRASH) {
        R.string.sb_main_deleted
    } else {
        R.string.sb_main_add_trash
    }

    private fun clickingSnackbarButton(note: Note2, isFavorite: Boolean, category: String) {
        btnSnackbar.setOnClickListener {
            if (category == CATEGORY_TRASH) {
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

    private fun setAnimation() {
        val animation = ObjectAnimator.ofInt(progressBar, "progress", 10000)
        animation.duration = 5900 // 6 second
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }

    private fun setTimer() {
        countDownTimer = object : CountDownTimer(6000, 100) {
            override fun onTick(leftTimeInMilliseconds: Long) {
                val seconds: Long = leftTimeInMilliseconds / 1000
                tvProgress.text = seconds.toString()
            }

            override fun onFinish() {
                snackbar?.dismiss()
            }
        }.start()
    }

    fun close() {
        snackbar?.dismiss()
        countDownTimer?.cancel()
    }
}