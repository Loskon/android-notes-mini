package com.loskon.noteminimalism3.backup.second;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loskon.noteminimalism3.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Ксастомный адаптер для ListView, чтобы отображать копии базы данных из папки.
 * А также удалять их
 */

public class ArrayAdapterFiles extends BaseAdapter {

    private final Context context;
    private final ArrayList<String> listFiles;
    private final File folder;
    private final File[] files;

    private static Callback callback;


    public static void registerCallBack(Callback callback) {
        ArrayAdapterFiles.callback = callback;
    }

    public ArrayAdapterFiles(Context context, ArrayList<String> listFiles,
                             File folder, File[] files) {
        this.context = context;
        this.listFiles = listFiles;
        this.folder = folder;
        this.files = files;
    }

    @Override
    public int getCount() {
        return listFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return listFiles;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = inflater.inflate(R.layout.row_for_import, null, true);
        }

        // initView
        TextView nameFiles = view.findViewById(R.id.txt_title_files);
        ImageButton delFile = view.findViewById(R.id.img_btn_delete_file);

        nameFiles.setText(listFiles.get(position).replace(".db", ""));

        delFile.setOnClickListener(v -> {

            SQLiteDatabase.deleteDatabase(new File(files[position].getPath()));

            listFiles.remove(position);
            notifyDataSetChanged();

            callBack();
        });

        return view;
    }

    public void deleteAll() {
        // Удалить все фалйы
        File[] files = BackupHelper.getListFile(folder);

        for (File file : files) {
            SQLiteDatabase.deleteDatabase(new File(file.getPath()));
        }

        listFiles.clear();
        notifyDataSetChanged();

        callBack();
    }

    private void callBack() {
        // Скрыть кнопку удаления и показать текст о том, что список пуст
        if (listFiles.size() == 0) {
            callback.callingBack();
        }
    }

    public interface Callback {
        void callingBack();
    }
}