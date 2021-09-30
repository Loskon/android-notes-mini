package com.loskon.noteminimalism3.ui.recyclerview.update

import android.widget.Filter
import com.loskon.noteminimalism3.model.Note2
import java.util.*

/**
 *
 */

class FilterCustomerSearchUpdate(
    private val adapter: NoteListAdapterUpdate,
    private var list: List<Note2>,
    private var listFiltered: List<Note2>
) : Filter() {

    override fun performFiltering(charSequence: CharSequence): FilterResults {
        val charString = charSequence.toString()

        list = if (charString.isEmpty()) {
            listFiltered
        } else {
            val filteredList = ArrayList<Note2>()

            for (note in listFiltered) {
                if (note.title.lowercase().contains(charString.lowercase())) {
                    filteredList.add(note)
                }
            }

            filteredList
        }

        val filterResults = FilterResults()
        filterResults.values = list

        return filterResults
    }

    @Suppress("UNCHECKED_CAST")
    override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
        //adapter.setNotes(filterResults.values as List<Note2>)
    }
}