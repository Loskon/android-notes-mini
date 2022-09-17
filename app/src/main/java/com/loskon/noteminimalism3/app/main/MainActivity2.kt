package com.loskon.noteminimalism3.app.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.activity.installTaskDescriptionColor
import com.loskon.noteminimalism3.managers.AppFont

class MainActivity2 : AppCompatActivity(R.layout.activity_main2) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppFont.set(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        installTaskDescriptionColor(R.color.task_description_color)
    }
}