package com.loskon.noteminimalism3.ui.recyclerview.profile

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.databinding.ItemProfileBinding
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.utils.getRadiusLinLay
import com.loskon.noteminimalism3.utils.getStrokeLinLay
import com.loskon.noteminimalism3.utils.setOnSingleClickListener


/**
 * Адаптер для работы со списком тренировочных профилей
 */

private val TAG = "MyLogs_${NoteListAdapter::class.java.simpleName}"

class NoteListAdapter() : RecyclerView.Adapter<NoteListViewHolder>() {

    private var colorStroke: Int = 0
    private var borderStroke: Int = 0
    private var radiusStroke: Int = 0
    private var color: Int = 0

    private var radiusStroke_dp = 0
    private var boredStroke_dp: Int = 0

    private var list = emptyList<Note2>()

    private var numItemSel: Int = 0
    private var isSelectionMode: Boolean = false

/*    fun setSettings(context: Context) {

    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val context: Context = parent.context

        radiusStroke_dp = context.getRadiusLinLay()
        boredStroke_dp = context.getStrokeLinLay()
        color = MyColor.getMyColor(context)

        val view: ItemProfileBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
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

            itemView.setOnSingleClickListener {
                handlingClick(note)
            }

            itemView.setOnLongClickListener {
                handlingLongClick(note)
                true
            }



            if (note.isChecked) {
                varForGradientDrawable(radiusStroke_dp, boredStroke_dp, color)
            } else {
                varForGradientDrawable(0, 0, Color.TRANSPARENT)
            }

            gradientDrawable.cornerRadius = radiusStroke.toFloat()
            gradientDrawable.setStroke(borderStroke, colorStroke)
            getLinearLayout.background = gradientDrawable
        }
    }

    private fun varForGradientDrawable(radius: Int, border: Int, color: Int) {
        radiusStroke = radius
        borderStroke = border
        colorStroke = color
    }

    private fun handlingClick(note: Note2) {
        if (isSelectionMode) {
            onItemSelection(note)
        } else {
            callback?.onItemClick(note)
        }
    }

    private fun handlingLongClick(note: Note2) {
        if (!isSelectionMode) startDelMode()
        onItemSelection(note)
    }

    private fun startDelMode() {
        isSelectionMode = true
        callback?.onDeleteMode(true)
        numItemSel = 0
    }

    private fun onItemSelection(note: Note2) {
        if (note.isChecked) {
            numItemSel--
        } else {
            numItemSel++
        }

        callback?.onSelectedItem(note)
        checkNumberSelectedItems()
    }

    private fun checkNumberSelectedItems() {
        val isSelectedAll: Boolean = numItemSel == itemCount
        callback?.onNumSelItem(isSelectedAll, numItemSel)
    }

    fun selectAllItems() {
        numItemSel = if (numItemSel == itemCount) {
            0
        } else {
            itemCount
        }

        checkNumberSelectedItems()
    }

    // Other methods
    fun setListNote(newList: List<Note2>) {
        val diffUtil = NoteDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil, false)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun setListNote2(newList: List<Note2>) {
        list = newList
        notifyDataSetChanged()
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

    // Callback
    interface OnItemClickListener {
        fun onItemClick(note: Note2)
        fun onSelectedItem(note: Note2)
        fun onDeleteMode(isDelMode: Boolean)
        fun onNumSelItem(isAll: Boolean, numSelItem: Int)
        fun onX()
    }

    companion object {
        private var callback: OnItemClickListener? = null

        fun setClickListener(clickListener: OnItemClickListener) {
            this.callback = clickListener
        }
    }
}