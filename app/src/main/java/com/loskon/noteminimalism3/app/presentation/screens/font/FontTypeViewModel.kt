package com.loskon.noteminimalism3.app.presentation.screens.font

import com.loskon.noteminimalism3.app.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.model.FontType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FontTypeViewModel : BaseViewModel() {

    private val fontTypeListState = MutableStateFlow<List<FontType>>(emptyList())
    val getFontTypeListState get() = fontTypeListState.asStateFlow()

    fun setFontList(fontTypes: List<FontType>) {
        launchErrorJob { fontTypeListState.emit(fontTypes) }
    }
}