package com.loskon.noteminimalism3.ui.snackbars

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 *
 */

abstract class BaseButtonSnackbar(
    private val context: Context,
    layout: View,
    private val anchorView: View,
    private val message: String,
    private val textButton: String
) {

    private val snackbar = Snackbar.make(layout, "", Snackbar.LENGTH_LONG)

    fun show() {
        val layout: SnackbarLayout = snackbar.view as SnackbarLayout
        val view: View = View.inflate(context, R.layout.snackbar_note_reset, null)
        val text: TextView = view.findViewById(R.id.snackbar_text_reset)
        val btnSnackbar: Button = view.findViewById(R.id.snackbar_action_note_reset)
        btnSnackbar.setTextColor(MyColor.getMyColor(context))
        layout.addView(view, 0)


        text.text = message
        btnSnackbar.text = textButton

        btnSnackbar.setOnSingleClickListener {
            onActionClick()
            snackbar.dismiss()
        }

        snackbar.anchorView = anchorView
        snackbar.show()
    }

    abstract fun onActionClick()
}