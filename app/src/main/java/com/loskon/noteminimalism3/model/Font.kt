package com.loskon.noteminimalism3.model

import android.graphics.Typeface

/**
 * Модель Font
 */

data class Font(
    var id: Int = 0,
    var title: String = "",
    var typeFace: Typeface? = null
)
