package com.loskon.noteminimalism3.backup.second

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.second.BackupSort.SortFileDate
import com.loskon.noteminimalism3.ui.dialogs.SheetListFiles
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import java.io.File
import java.util.*

/**
 *
 */

class FilesAdapter(
    private val context: Context,
    private val sheetListFiles: SheetListFiles
) :
    BaseAdapter() {

    private val folder = BackupPath.getFolder(context)
    private val files: Array<File> = BackupHelper.getListFile(folder)
    private val list: MutableList<File> = files.toMutableList()

    init {
        Collections.sort(list, SortFileDate())
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(context, R.layout.row_list_restore2, null)
        val file = list[position]

        val nameFiles: TextView = view.findViewById(R.id.tv_title_file)
        val cardView: CardView = view.findViewById(R.id.card_view)
        val delFile: MaterialButton = view.findViewById(R.id.button2)

        nameFiles.text = file.name.replace(".db", "")

        cardView.setOnSingleClickListener {
            BackupDb(context).restoreDatabase(file.path)
            sheetListFiles.asd()
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