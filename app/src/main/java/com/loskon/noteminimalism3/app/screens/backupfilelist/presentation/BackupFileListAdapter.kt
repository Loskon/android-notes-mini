package com.loskon.noteminimalism3.app.screens.backupfilelist.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.databinding.ItemFileBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding
import java.io.File

class BackupFileListAdapter : RecyclerView.Adapter<BackupFileListAdapter.BackupFileListViewHolder>() {

    private var list: List<File> = emptyList()

    private var onItemClickListener: ((File) -> Unit)? = null
    private var onItemDeleteClickListener: ((File) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackupFileListViewHolder {
        return BackupFileListViewHolder(parent.viewBinding(ItemFileBinding::inflate))
    }

    override fun onBindViewHolder(holder: BackupFileListViewHolder, position: Int) {
        val file = list[position]

        with(holder.binding) {
            tvTitleFile.text = file.name.replace(BACKUP_FILE_EXTENSION, "")
            btnDelFile.setDebounceClickListener { onItemDeleteClickListener?.invoke(file) }
            root.setDebounceClickListener { onItemClickListener?.invoke(file) }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateFileList(list: List<File>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: ((File) -> Unit)?) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemDeleteClickListener(onItemDeleteClickListener: ((File) -> Unit)?) {
        this.onItemDeleteClickListener = onItemDeleteClickListener
    }

    class BackupFileListViewHolder(val binding: ItemFileBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val BACKUP_FILE_EXTENSION = ".db"
    }
}