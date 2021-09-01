package com.loskon.noteminimalism3.ui.listview

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.second.BackupDb
import com.loskon.noteminimalism3.backup.second.BackupFilter
import com.loskon.noteminimalism3.backup.second.BackupPath
import com.loskon.noteminimalism3.backup.second.BackupSort.SortFileDate
import com.loskon.noteminimalism3.ui.sheets.SheetListFiles
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import java.io.File
import java.util.*

/**
 * Кастомный адаптер для вывода списка файлов резервных копий
 */

class FilesAdapter(
    private val context: Context,
    private val sheetListFiles: SheetListFiles
) :
    BaseAdapter() {

    private val folder = BackupPath.getFolder(context)
    private val files: Array<File> = BackupFilter.getListFile(folder)
    private val list: MutableList<File> = files.toMutableList()

    init {
        Collections.sort(list, SortFileDate())
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(context, R.layout.item_file, null)
        val file = list[position]

        val nameFiles: TextView = view.findViewById(R.id.tv_title_file)
        val cardView: CardView = view.findViewById(R.id.card_view_file)
        val delFile: MaterialButton = view.findViewById(R.id.btn_del_file)

        nameFiles.text = file.name.replace(".db", "")

        cardView.setOnSingleClickListener {
            BackupDb(context).restoreDatabase(file.path)
            sheetListFiles.dismissSheet()
        }

        delFile.setOnSingleClickListener { deleteItem(file) }

        return view
    }

    private fun deleteItem(file: File) {
        file.delete()
        list.remove(file)
        notifyDataSetChanged()
        sheetListFiles.checkEmptyListFiles()
    }

    fun deleteAll() {
        for (file in files) {
            file.delete()
        }
        list.clear()
        notifyDataSetChanged()
        sheetListFiles.checkEmptyListFiles()
    }

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): File = list[position]

    override fun getItemId(position: Int): Long = position.toLong()
}