package com.loskon.noteminimalism3.app.presentation.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.activity.installTaskDescriptionColor
import com.loskon.noteminimalism3.ui.activities.MainActivity

class MainActivity2 : AppCompatActivity(R.layout.activity_main2) {

    override fun onAttachedToWindow() {
        installTaskDescriptionColor(getColor(R.color.task_description_color))
        super.onAttachedToWindow()
    }

    companion object {
        fun makeIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}