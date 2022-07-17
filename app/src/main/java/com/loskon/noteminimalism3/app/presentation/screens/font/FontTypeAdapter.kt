package com.loskon.noteminimalism3.app.presentation.screens.font

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.app.base.extension.view.setColorKtx
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.databinding.ItemFontBinding
import com.loskon.noteminimalism3.model.FontType
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding

class FontTypeAdapter : RecyclerView.Adapter<FontTypeAdapter.FontNewViewHolder>() {

    private var onClickListener: ((fontType: FontType, position: Int) -> Unit)? = null

    private var list: List<FontType> = emptyList()

    private var color: Int = 0
    private var lastCheckedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontNewViewHolder {
        color = AppPreference.getColor(parent.context)
        lastCheckedPosition = AppPreference.getFontType(parent.context)
        return FontNewViewHolder(parent.viewBinding(ItemFontBinding::inflate))
    }

    override fun onBindViewHolder(holder: FontNewViewHolder, position: Int) {
        val fontType: FontType = list[position]

        with(holder.binding) {
            rbFontCard.setColorKtx(color)
            tvTitleFontCard.text = fontType.title
            tvTitleFontCard.typeface = fontType.typeFace
            tvFontExampleCard.typeface = fontType.typeFace
            rbFontCard.isChecked = (position == lastCheckedPosition)
            root.setDebounceClickListener { onItemClick(fontType, position) }
        }
    }

    override fun getItemCount(): Int = list.size

    private fun onItemClick(fontType: FontType, position: Int) {
        lastCheckedPosition = position
        notifyItemRangeChanged(0, itemCount)
        onClickListener?.invoke(fontType, position)
    }

    fun updateFontList(newList: List<FontType>) {
        list = newList
        notifyItemRangeChanged(0, itemCount)
    }

    fun setItemOnClickListener(onClickListener: ((fontType: FontType, position: Int) -> Unit)?) {
        this.onClickListener = onClickListener
    }

    class FontNewViewHolder(val binding: ItemFontBinding) : RecyclerView.ViewHolder(binding.root)
}