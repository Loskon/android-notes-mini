package com.loskon.noteminimalism3.base.widget.preference

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.preference.EditTextPreference
import com.loskon.noteminimalism3.base.extension.view.setFilterKtx

/**
 * EditTextPreference to enter and save only integers
 */
@SuppressLint("PrivateResource")
class NumberEditTextPreference(
    context: Context,
    attrs: AttributeSet
) : EditTextPreference(context, attrs) {

    private var defaultValue: Int = 0

    init {
        context.theme.obtainStyledAttributes(attrs, androidx.preference.R.styleable.Preference, 0, 0).apply {
            defaultValue = getString(androidx.preference.R.styleable.Preference_defaultValue)?.toInt() ?: 0
        }

        setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.setFilterKtx(pattern = "[^0-9]", maxLength = 8)
            editText.setSelection(editText.text.length)
        }
    }

    override fun getPersistedString(defaultReturnValue: String?): String {
        return getPersistedInt(defaultValue).toString()
    }

    override fun persistString(value: String?): Boolean {
        val savedValue = if (value.isNullOrEmpty()) 0 else Integer.valueOf(value)
        return persistInt(savedValue)
    }

    override fun getSummary(): CharSequence {
        return if (super.getSummary() == "Not set") {
            "0"
        } else {
            super.getSummary().toString().toInt().toString()
        }
    }
}