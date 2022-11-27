package com.loskon.noteminimalism3.app.screens.notelist.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.base.datetime.formattedString
import com.loskon.noteminimalism3.base.extension.view.setBackgroundTintColorKtx
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.extension.view.setShortLongClickListener
import com.loskon.noteminimalism3.base.extension.view.setTextSizeKtx
import com.loskon.noteminimalism3.base.extension.view.setVisibleKtx
import com.loskon.noteminimalism3.databinding.ItemNoteNewBinding
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.recyclerview.notes.NoteDiffUtil
import com.loskon.noteminimalism3.viewbinding.viewBinding

@SuppressLint("NotifyDataSetChanged")
class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>() {

    private var list: List<Note> = emptyList()

    private var onItemClick: ((Note, Int) -> Unit)? = null
    private var onItemLongClick: ((Note, Int) -> Unit)? = null

    private var linearListType: Boolean = true
    private var hasOneSizeCards: Boolean = false
    private var color: Int = 0
    private var titleFontSize: Int = 0
    private var dateFontSize: Int = 0
    private var numberLines: Int = 0

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        hasOneSizeCards = AppPreference.hasOneSizeCards(parent.context)
        color = AppPreference.getColor(parent.context)
        titleFontSize = AppPreference.getTitleFontSize(parent.context)
        dateFontSize = AppPreference.getDateFontSize(parent.context)
        numberLines = AppPreference.getNumberLines(parent.context)
        return NoteListViewHolder(parent.viewBinding(ItemNoteNewBinding::inflate))
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val note = list[position]

        with(holder.binding) {
            tvCardNoteTitle.setTextSizeKtx(titleFontSize)
            tvCardNoteTitle.maxLines = numberLines
            tvCardNoteTitle.minLines = getMinLines()
            tvCardNoteDate.setTextSizeKtx(dateFontSize)
            viewFavorite.setBackgroundTintColorKtx(color)
            root.strokeColor = if (note.isChecked) color else Color.TRANSPARENT

            tvCardNoteTitle.text = note.title.trim()
            tvCardNoteDate.text = note.createdDate.formattedString()
            viewFavorite.setVisibleKtx(note.isFavorite)
            root.setDebounceClickListener { onItemClick?.invoke(note, position) }
            root.setShortLongClickListener { onItemLongClick?.invoke(note, position) }
        }
    }

    private fun getMinLines(): Int {
        return if (linearListType) {
            MAX_LINES
        } else {
            if (hasOneSizeCards) numberLines else MAX_LINES
        }
    }

    fun getItems(): List<Note> = list

    fun getNote(position: Int): Note = list[position]

    fun setQuicklyNoteList(list: List<Note>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setNoteList(list: List<Note>) {
        val diffUtil = NoteDiffUtil(this.list, list)
        val diffResult = DiffUtil.calculateDiff(diffUtil, false)
        this.list = list
        diffResult.dispatchUpdatesTo(this)
    }

    fun setLinearList(hasLinearList: Boolean) {
        this.linearListType = hasLinearList
    }

    fun setOnItemClickListener(onItemClickListener: ((Note, Int) -> Unit)?) {
        this.onItemClick = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: ((Note, Int) -> Unit)?) {
        this.onItemLongClick = onItemLongClickListener
    }

    class NoteListViewHolder(val binding: ItemNoteNewBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val MAX_LINES = 1
    }
}