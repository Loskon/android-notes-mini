package com.loskon.noteminimalism3.ui.listview

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.second.BackupSort
import com.loskon.noteminimalism3.ui.sheets.SheetRestoreDateBase
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import java.io.File
import java.util.*

/**
 * Кастомный адаптер для вывода списка файлов резервных копий
 */

class FilesAdapter(private val sheetDialog: SheetRestoreDateBase) :
    BaseAdapter() {

    private var list: ArrayList<File> = arrayListOf()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(parent?.context, R.layout.item_file, null)
        val file = list[position]

        val nameFiles: TextView = view.findViewById(R.id.tv_title_file)
        val cardView: CardView = view.findViewById(R.id.card_view_file)
        val delFile: MaterialButton = view.findViewById(R.id.btn_del_file)

        nameFiles.text = file.name.replace(".db", "")

        cardView.setOnSingleClickListener {
            sheetDialog.restoreDateBase(file.path)
        }

        delFile.setOnSingleClickListener {
            remove(file)
        }

        return view
    }

    private fun remove(file: File) {
        file.delete()
        list.remove(file)
        notifyDataSetChanged()
        sheetDialog.checkEmptyFilesList()
    }

    fun removeAll(files: Array<File>?) {
        if (files != null) {
            for (file in files) {
                file.delete()
            }

            list.clear()
            notifyDataSetChanged()
            sheetDialog.checkEmptyFilesList()
        }
    }

    fun setFilesList(newList: Array<File>?) {
        if (newList != null) {
            list = newList.toCollection(ArrayList())
            Collections.sort(list, BackupSort.SortFileDate())
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): File = list[position]

    override fun getItemId(position: Int): Long = position.toLong()
}