package com.loskon.noteminimalism3.app.presentation.screens

import com.loskon.noteminimalism3.app.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.model.Font
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FontViewModel : BaseViewModel() {

    private val fontListState = MutableStateFlow<List<Font>>(emptyList())
    val getFontListState get() = fontListState.asStateFlow()

    fun setFontList(fonts: List<Font>) {
        launchErrorJob { fontListState.emit(fonts) }
    }
}