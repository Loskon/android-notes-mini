package com.loskon.noteminimalism3.db.backup;

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
import java.util.Objects;

/**
 *
 */

public class ArrayAdapterFiles extends BaseAdapter {

    private final Context context;
    private final ArrayList<String> listFiles;
    private final File folder;

    private static CallbackArrAdapterEmpty cbArrAdapterEmptyListener;

    public void regArrAdapterEmpty(CallbackArrAdapterEmpty cbArrAdapterEmptyListener){
        ArrayAdapterFiles.cbArrAdapterEmptyListener = cbArrAdapterEmptyListener;
    }

    public ArrayAdapterFiles(Context context, ArrayList<String> listFiles, File folder) {
        this.context = context;
        this.listFiles = listFiles;
        this.folder = folder;
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
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
             view = inflater.inflate(R.layout.single_row_offers, null,true);
        }

        // initView
        TextView txtTitleFiles = view.findViewById(R.id.txt_title_files);
        ImageButton deleteFile = view.findViewById(R.id.img_btn_delete_file);

        txtTitleFiles.setText(listFiles
                .get(position).replace(".db", ""));

        if (folder.exists()) {

            File[] files = folder.listFiles();

            deleteFile.setOnClickListener(v -> {
                // Delete file
                SQLiteDatabase.deleteDatabase(new File(Objects
                        .requireNonNull(files)[position].getPath()));
                listFiles.remove(position);
                notifyDataSetChanged();

                callingBack();
            });
        }

        return view;
    }

    public void deleteAll(File[] files) {
        // Delete all SQLite file

        //        File dir = new File(path +
        //                File.separator + context.getResources().getString(R.string.app_name));
        //        if (dir.isDirectory())
        //        {
        //            String[] children = dir.list();
        //            for (String child : children) {
        //                new File(dir, child).deleteOnExit();
        //            }
        //        }

        for (int i = 0; i < listFiles.size(); i++) {
            SQLiteDatabase.deleteDatabase(new File(files[i].getPath()));
        }

        listFiles.clear();
        notifyDataSetChanged();

        callingBack();
    }

    private void callingBack() {
        if (listFiles.size() == 0 && cbArrAdapterEmptyListener != null) {
            cbArrAdapterEmptyListener.callingBackArrAdapterEmpty();
        }
    }

    public interface CallbackArrAdapterEmpty {
        void callingBackArrAdapterEmpty();
    }
}