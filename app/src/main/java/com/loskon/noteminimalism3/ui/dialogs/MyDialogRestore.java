package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.db.backup.ArrayAdapterFiles;
import com.loskon.noteminimalism3.db.backup.BackupAndRestoreDatabase;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.RestoreHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.loskon.noteminimalism3.R.style.MaterialAlertDialog_Rounded;

public class MyDialogRestore {

    private final Activity activity;
    private TextView txtEmptyRestore;
    private ImageButton btnRemoveAll;
    private ArrayList<String> listFiles;

    public MyDialogRestore(Activity activity) {
        this.activity = activity;
    }

    public void callDialogRestore(File folder) {
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(activity,
                MaterialAlertDialog_Rounded)
                .setView(R.layout.dialog_restore)
                .create();

        // View settings
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.getWindow().setGravity(Gravity.CENTER);

        alertDialog.show();

        // initView
        ListView listViewFiles = alertDialog.findViewById(R.id.list_view_files);
        txtEmptyRestore = alertDialog.findViewById(R.id.txt_empty_restore);
        btnRemoveAll = alertDialog.findViewById(R.id.img_btn_remove_all);
        Button btnCancel = alertDialog.findViewById(R.id.btn_cancel_restore);

        ArrayAdapterFiles arrayAdapterFiles;
        File[] files = RestoreHelper.getListFile(folder);

        // assert
        assert listViewFiles != null;
        assert btnCancel != null;
        assert files != null;

        int color = MyColor.getColorCustom(activity);
        btnCancel.setTextColor(color);

        //RestoreHelper.purgeLogFiles(files);

        listFiles = new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(RestoreHelper.getListFile(folder)));
        Collections.sort(fileList, new RestoreHelper.SortFileDate());

        for (File file : fileList) {
            listFiles.add(file.getName());
        }

        checkEmptyListFiles();

        // Устанавливам адаптер
        arrayAdapterFiles = new ArrayAdapterFiles(activity, listFiles, folder);
        listViewFiles.setAdapter(arrayAdapterFiles);

        // Click listView
        listViewFiles.setOnItemClickListener((adapterView, view, position, l) -> {
            BackupAndRestoreDatabase.restoreDatabase(activity, files[position].getPath());
            alertDialog.dismiss();
        });

        // Click deleteAll
        btnRemoveAll.setOnClickListener(view -> {
            arrayAdapterFiles.notifyDataSetChanged();
            arrayAdapterFiles.deleteAll(files);
        });

        // Click cancel
        btnCancel.setOnClickListener(v -> alertDialog.dismiss());

        // Callback from CustomArrayAdapter
        arrayAdapterFiles.regArrAdapterEmpty(this::thisListEmpty);
    }

    private void checkEmptyListFiles() {
        if (listFiles.size() == 0) {
            thisListEmpty();
        } else {
            txtEmptyRestore.setVisibility(View.INVISIBLE);
            btnRemoveAll.setVisibility(View.VISIBLE);
        }
    }

    private void thisListEmpty() {
        txtEmptyRestore.setVisibility(View.VISIBLE);
        btnRemoveAll.setVisibility(View.INVISIBLE);
    }
}
