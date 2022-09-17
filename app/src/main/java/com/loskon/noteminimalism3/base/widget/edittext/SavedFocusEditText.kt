package com.loskon.noteminimalism3.base.widget.edittext

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class SavedFocusEditText constructor(
    context: Context,
    attrs: AttributeSet
) : AppCompatEditText(context, attrs) {

    override fun onSaveInstanceState(): Parcelable {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.hasFocus = isFocused
        savedState.cursorPosition = selectionEnd
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            updateVisibility(state.hasFocus, state.cursorPosition)
            requestLayout()
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private fun updateVisibility(hasFocus: Boolean, cursorPosition: Int) {
        if (hasFocus) {
            requestFocus()
            setSelection(cursorPosition)
        }
    }

    internal class SavedState : BaseSavedState {

        var hasFocus = false
        var cursorPosition = 0

        constructor(superState: Parcelable?) : super(superState)

        constructor(source: Parcel) : super(source) {
            hasFocus = source.readInt() != 0
            cursorPosition = source.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(if (hasFocus) 1 else 0)
            out.writeInt(cursorPosition)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}