package com.loskon.noteminimalism3.ui.recyclerview

import android.widget.Filter
import com.loskon.noteminimalism3.model.Note
import java.util.*

/**
 *
 */

class FilterCustomerSearch(
    private val adapter: MyRecyclerViewAdapter,
    private var noteList: ArrayList<Note>,
    private var toSearchList: ArrayList<Note>
) : Filter() {

    override fun performFiltering(charSequence: CharSequence): FilterResults? {
        val charString = charSequence.toString()

        noteList = if (charString.isEmpty()) {
            adapter.setSearchMode(false)
            toSearchList
        } else {
            adapter.setSearchMode(true)

            val filteredList = ArrayList<Note>()

            for (note in toSearchList) {
                if (note.title.lowercase().contains(charString.lowercase())) {
                    filteredList.add(note)
                }
            }

            filteredList
        }

        val filterResults = FilterResults()
        filterResults.values = noteList
        return filterResults
    }

    @Suppress("UNCHECKED_CAST")
    override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
        adapter.notes = filterResults.values as ArrayList<Note?>
        adapter.notifyDataSetChanged()
    }
}