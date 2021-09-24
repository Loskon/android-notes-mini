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
import com.loskon.noteminimalism3.ui.activities.update.MainActivityUpdate
import com.loskon.noteminimalism3.viewmodel.AppShortsCommand
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Companion.CATEGORY_TRASH

/**
 * Кастомный Snackbar с таймером
 */

class SnackbarUndoUpdate(
    private val activity: MainActivityUpdate,
    private val shortsCommand: AppShortsCommand
) {

    private val fab: FloatingActionButton = activity.getFab
    private val coordLayout: CoordinatorLayout = activity.getCoordLayout

    private var snackbarMain: Snackbar? = null
    private var countDownTimer: CountDownTimer? = null

    private lateinit var btnSnackbar: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textProgress: TextView
    private lateinit var textTitle: TextView

    fun show(note: Note2, isFav: Boolean, category: String) {
        countDownTimer?.cancel()
        snackbarMain?.dismiss()

        snackbarMain = Snackbar.make(coordLayout, "", Snackbar.LENGTH_INDEFINITE)
        snackbarMain?.anchorView = fab

        val layout = snackbarMain?.view as SnackbarLayout
        val view: View = View.inflate(activity, R.layout.snackbar_swipe, null)

        initViews(view)
        configViews(note, isFav, category)
        setAnimation()
        setTimer()

        layout.addView(view, 0)
        snackbarMain?.show()
    }

    private fun initViews(snackView: View) {
        btnSnackbar = snackView.findViewById(R.id.snackbar_btn)
        progressBar = snackView.findViewById(R.id.snackbar_progress_bar)
        textProgress = snackView.findViewById(R.id.snackbar_text_progress)
        textTitle = snackView.findViewById(R.id.snackbar_text_title)
    }

    private fun configViews(note: Note2, isFav: Boolean, category: String) {
        btnSnackbar.setTextColor(MyColor.getMyColor(activity))
        progressBar.progress = 0
        progressBar.max = 10000
        textTitle.text = activity.getString(getMessage(category))

        btnSnackbar.setOnClickListener {
            if (category == CATEGORY_TRASH) {
                shortsCommand.insert(note)
            } else {
                note.isFavorite = isFav
                note.isDelete = false
                shortsCommand.update(note)
                activity.UP()
            }

            snackbarMain?.dismiss()
        }
    }

    private fun getMessage(category: String): Int = if (category == CATEGORY_TRASH) {
        R.string.sb_main_deleted
    } else {
        R.string.sb_main_add_trash
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
                val seconds = leftTimeInMilliseconds / 1000
                textProgress.text = seconds.toString()
            }

            override fun onFinish() {
                snackbarMain?.dismiss()
            }
        }.start()
    }

    fun close() {
        snackbarMain?.dismiss()
        countDownTimer?.cancel()
    }
}