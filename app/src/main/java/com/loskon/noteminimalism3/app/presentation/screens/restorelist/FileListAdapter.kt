package com.loskon.noteminimalism3.app.presentation.screens.restorelist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.databinding.ItemFileBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding
import java.io.File

class FileListAdapter : RecyclerView.Adapter<FileListAdapter.RestoreListViewHolder>() {

    private var list: List<File> = emptyList()

    private var onItemClickListener: ((File) -> Unit)? = null
    private var onItemDeleteClickListener: ((File) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestoreListViewHolder {
        return RestoreListViewHolder(parent.viewBinding(ItemFileBinding::inflate))
    }

    override fun onBindViewHolder(holder: RestoreListViewHolder, position: Int) {
        val file = list[position]

        with(holder.binding) {
            tvTitleFile.text = file.name.replace(".db", "")
            btnDelFile.setDebounceClickListener { onItemClickListener?.invoke(file) }
            root.setDebounceClickListener { onItemDeleteClickListener?.invoke(file) }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateFileList(list: List<File>) {
        this.list = list
        notifyItemRangeChanged(0, itemCount)
    }

    fun setOnItemClickListener(onItemClickListener: ((File) -> Unit)?) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemDeleteClickListener(onItemDeleteClickListener: ((File) -> Unit)?) {
        this.onItemDeleteClickListener = onItemDeleteClickListener
    }

    class RestoreListViewHolder(val binding: ItemFileBinding) : RecyclerView.ViewHolder(binding.root)
}