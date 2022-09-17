package com.loskon.noteminimalism3.ui.listview

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.files.SortFileDatetime
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import java.io.File
import java.util.*

/**
 * Адаптер для работы со списком файлов
 */

class FileBaseAdapter : BaseAdapter() {

    private var list: ArrayList<File> = arrayListOf()

    private lateinit var clickListener: FileClickListener

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(parent?.context, R.layout.item_file, null)
        val nameFiles: TextView = view.findViewById(R.id.tv_title_file)
        val cardView: CardView = view.findViewById(R.id.card_view_file)
        val delFile: MaterialButton = view.findViewById(R.id.btn_del_file)

        val file = list[position]

        nameFiles.text = file.name.replace(".db", "")
        cardView.setDebounceClickListener { clickListener.onFileClick(file) }
        delFile.setDebounceClickListener { remove(file) }

        return view
    }

    private fun remove(file: File) {
        file.delete()
        list.remove(file)
        notifyDataSetChanged()
        clickListener.onCheckEmpty()
    }

    fun setFilesList(newList: Array<File>?) {
        if (newList != null) {
            list = newList.toCollection(ArrayList())
            Collections.sort(list, SortFileDatetime())
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): File = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    //--- interface --------------------------------------------------------------------------------
    interface FileClickListener {
        fun onFileClick(file: File)
        fun onCheckEmpty()
    }

    fun registerFileClickListener(clickListener: FileClickListener) {
        this.clickListener = clickListener
    }
}