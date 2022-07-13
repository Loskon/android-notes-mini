package com.loskon.noteminimalism3.app.base.extension.view

import android.text.InputFilter
import android.widget.EditText

fun EditText.setFilterKtx(
    pattern: String,
    maxLength: Int = -1
) {
    val characterFilter = InputFilter { source, start, end, _, _, _ ->
        source?.subSequence(start, end)?.replace(Regex(pattern), "")?.trim()
    }
    val lengthFilter = InputFilter.LengthFilter(maxLength)

    filters = if (maxLength == -1) {
        arrayOf(characterFilter)
    } else {
        arrayOf(characterFilter, lengthFilter)
    }
}