package com.loskon.noteminimalism3.other

import androidx.appcompat.widget.SearchView

abstract class ShortQueryTextListener : SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        onShortQueryTextChange(newText?.trim())
        return true
    }

    abstract fun onShortQueryTextChange(query: String?)
}