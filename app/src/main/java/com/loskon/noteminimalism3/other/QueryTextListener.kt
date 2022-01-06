package com.loskon.noteminimalism3.other

import android.text.TextUtils
import androidx.appcompat.widget.SearchView

/**
 * OnQueryTextListener без onQueryTextSubmit
 */

abstract class QueryTextListener : SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val query: String? = newText?.trim()

        if (TextUtils.isEmpty(query)) {
            queryTextChange(null)
        } else {
            queryTextChange(query)
        }

        return true
    }


    abstract fun queryTextChange(query: String?)
}