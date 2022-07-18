package com.loskon.noteminimalism3.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.loskon.noteminimalism3.managers.ColorManager
import com.loskon.noteminimalism3.managers.AppFont

/**
 * Изменение шрифта и цвета приложения
 */

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppFont.set(this)
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        ColorManager.installAppColor(this)
    }
}