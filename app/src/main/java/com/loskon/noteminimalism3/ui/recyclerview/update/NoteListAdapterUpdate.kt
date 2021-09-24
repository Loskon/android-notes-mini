package com.loskon.noteminimalism3.ui.recyclerview.update

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.databinding.ItemProfileBinding
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.recyclerview.profile.NoteDiffUtil
import com.loskon.noteminimalism3.ui.recyclerview.profile.NoteListViewHolder
import com.loskon.noteminimalism3.utils.setOnSingleClickListener


/**
 * Адаптер для работы со списком заметок
 */

class NoteListAdapterUpdate : SelectableAdapterUpdate<NoteListViewHolder>() {

    private var list = emptyList<Note2>()

    private var isSelectionMode: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val view: ItemProfileBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_profile,
            parent,
            false
        )

        return NoteListViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val note = list[position]
        val gradientDrawable = GradientDrawable()

        holder.apply {
            bind(note)

            itemView.apply {
                setOnSingleClickListener {
                    handlingClick(note, position)
                }

                setOnLongClickListener {
                    handlingLongClick(note, position)
                    true
                }
            }

            if (isSelected(position)) {
                setVarGradDraw(radiusStrokeDp, boredStrokeDp, color)
            } else {
                setVarGradDraw(0, 0, 0)
            }

            gradientDrawable.apply {
                cornerRadius = radiusStroke.toFloat()
                setStroke(borderStroke, colorStroke)
                getLinearLayout.background = this
            }
        }
    }

    ///

    private fun handlingClick(note: Note2, position: Int) {
        if (isSelectionMode) {
            toggleSelection(note, position)

            //helper.onItemSelection(note)
            //notifyItemChanged(position)
        } else {
            callback?.onItemClick(note)
        }
    }


    private fun handlingLongClick(note: Note2, position: Int) {
        if (!isSelectionMode) startDelMode()

        toggleSelection(note, position)

        callback?.onSelectedItem(note, getSelectedItemCount())

        //helper.onItemSelection(note)
        //notifyItemChanged(position)
    }

    private fun startDelMode() {
        isSelectionMode = true
        //callback?.onDeleteMode(true)
    }


    private fun checkNumberSelectedItems() {
        //val isSelectedAll: Boolean = numItemSel == itemCount
        //callback?.onNumSelItem(isSelectedAll, numItemSel)
    }

    fun selectAllItems() {
/*        numItemSel = if (numItemSel == itemCount) {
            0
        } else {
            itemCount
        }

        checkNumberSelectedItems()*/
    }

    fun getItemNote(position: Int): Note2 {
        return list[position]
    }

    fun disableDeleteMode() {
        isSelectionMode = false
    }

    fun getListNote(): List<Note2> {
        return list
    }

    // Внешние методы
    fun setViewSizes(radiusStrokeDp: Int, boredStrokeDp: Int) {
        this.radiusStrokeDp = radiusStrokeDp
        this.boredStrokeDp = boredStrokeDp
    }

    fun setViewColor(color: Int) {
        this.color = color
    }

    fun setListNote(newList: List<Note2>) {
        val diffUtil = NoteDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil, false)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }


    // Callback
    interface CallbackAdapter {
        fun onItemClick(note: Note2)
        fun onSelectedItem(note: Note2, size: Int)
        fun onDeleteMode(isDelMode: Boolean)
        fun onNumSelItem(isAll: Boolean, numSelItem: Int)
        fun onX()
    }

    companion object {
        private val TAG = "MyLogs_${NoteListAdapterUpdate::class.java.simpleName}"

        private var callback: CallbackAdapter? = null

        fun callbackAdapterListener(callback: CallbackAdapter) {
            this.callback = callback
        }
    }
}