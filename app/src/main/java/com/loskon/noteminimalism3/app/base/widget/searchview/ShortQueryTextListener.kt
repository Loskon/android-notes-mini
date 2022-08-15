package com.loskon.noteminimalism3.app.base.widget.searchview

import androidx.appcompat.widget.SearchView

open class ShortQueryTextListener(
    val onTextChange: (query: String?) -> Unit
) : SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(queryText: String?): Boolean {
        val query = queryText?.trim()

        if (query.isNullOrEmpty()) {
            onTextChange(null)
        } else {
            onTextChange(query)
        }

        return true
    }
}