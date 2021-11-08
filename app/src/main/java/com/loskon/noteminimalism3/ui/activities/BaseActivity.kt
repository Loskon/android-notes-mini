package com.loskon.noteminimalism3.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.loskon.noteminimalism3.other.FontManager
import com.loskon.noteminimalism3.utils.ColorManager

/**
 * Переопределение activity для установки шрифта и цветов
 */

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        FontManager.setFont(this)
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        ColorManager.setColorApp(this)
    }
}