package com.loskon.noteminimalism3.app.presentation.screens

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.databinding.ItemFontBinding
import com.loskon.noteminimalism3.managers.setColorKtx
import com.loskon.noteminimalism3.model.Font
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding

class FontNewAdapter : RecyclerView.Adapter<FontNewAdapter.FontNewViewHolder>() {

    private var onClickListener: ((font: Font, position: Int) -> Unit)? = null

    private var list: List<Font> = emptyList()

    private var color: Int = 0
    private var lastCheckedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontNewViewHolder {
        color = AppPreference.getColor(parent.context)
        lastCheckedPosition = AppPreference.getFontType(parent.context)
        return FontNewViewHolder(parent.viewBinding(ItemFontBinding::inflate))
    }

    override fun onBindViewHolder(holder: FontNewViewHolder, position: Int) {
        val font: Font = list[position]

        with(holder.binding) {
            tvTitleFontCard.text = font.title
            tvTitleFontCard.typeface = font.typeFace
            tvFontExampleCard.typeface = font.typeFace
            rbFontCard.setColorKtx(color)
            rbFontCard.isChecked = (position == lastCheckedPosition)
            root.setDebounceClickListener { onItemClick(font, position) }
        }
    }

    override fun getItemCount(): Int = list.size

    private fun onItemClick(font: Font, position: Int) {
        lastCheckedPosition = position
        notifyItemRangeChanged(0, itemCount)
        onClickListener?.invoke(font, position)
    }

    fun updateFontList(newList: List<Font>) {
        list = newList
        notifyItemRangeChanged(0, itemCount)
    }

    fun setItemOnClickListener(onClickListener: ((font: Font, position: Int) -> Unit)?) {
        this.onClickListener = onClickListener
    }

    inner class FontNewViewHolder(val binding: ItemFontBinding) : RecyclerView.ViewHolder(binding.root)
}