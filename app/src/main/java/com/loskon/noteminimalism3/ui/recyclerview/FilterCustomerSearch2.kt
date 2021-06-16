package com.loskon.noteminimalism3.ui.recyclerview

import android.widget.Filter
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.recyclerview.profile.NoteListAdapter
import java.util.*

/**
 *
 */

class FilterCustomerSearch2(
    private val adapter: NoteListAdapter,
    private var noteList: List<Note2>,
    private var toSearchList: List<Note2>
) : Filter() {

    override fun performFiltering(charSequence: CharSequence): FilterResults? {
        val charString = charSequence.toString()

        noteList = if (charString.isEmpty()) {
            toSearchList
        } else {
            val filteredList = ArrayList<Note2>()

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
        adapter.setNotes(filterResults.values as List<Note2>)
        adapter.notifyDataSetChanged()
    }
}