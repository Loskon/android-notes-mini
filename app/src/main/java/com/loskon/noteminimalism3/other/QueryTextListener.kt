package com.loskon.noteminimalism3.other

import androidx.appcompat.widget.SearchView

/**
 * OnQueryTextListener без onQueryTextSubmit
 */

abstract class QueryTextListener : SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        queryTextChange(newText?.trim())
        return true
    }

    abstract fun queryTextChange(query: String?)
}