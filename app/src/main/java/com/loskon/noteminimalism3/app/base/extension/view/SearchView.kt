package com.loskon.noteminimalism3.app.base.extension.view

import android.widget.EditText
import androidx.appcompat.widget.SearchView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.widget.searchview.ShortQueryTextListener
import com.loskon.noteminimalism3.utils.hideKeyboard
import com.loskon.noteminimalism3.utils.showKeyboard

fun SearchView.showKeyboard() {
    findViewById<EditText>(R.id.search_src_text).showKeyboard()
}

fun SearchView.hideKeyboard() {
    findViewById<EditText>(R.id.search_src_text).hideKeyboard()
}

fun SearchView.setShortQueryTextListener(onTextChange: (queryText: String?) -> Unit) {
    setOnQueryTextListener(ShortQueryTextListener { queryText -> onTextChange(queryText) })
}