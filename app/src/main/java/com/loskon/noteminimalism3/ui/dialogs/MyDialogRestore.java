package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.backup.second.ArrayAdapterFiles;
import com.loskon.noteminimalism3.backup.second.BackupDb;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.backup.second.BackupSort;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MyDialogRestore {

    private final Activity activity;
    private TextView txtEmptyRestore;
    private ImageButton btnRemoveAll;
    private ArrayList<String> listFiles;

    private static CallbackRestoreNotes cbRestoreNotes;

    public void regCallbackRestoreNotes(CallbackRestoreNotes cbRestoreNotes) {
        MyDialogRestore.cbRestoreNotes = cbRestoreNotes;
    }

    public MyDialogRestore(Activity activity) {
        this.activity = activity;
    }

    public void callDialogRestore(File folder) {
        AlertDialog alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_restore);
        alertDialog.show();

        // initView
        ListView listViewFiles = alertDialog.findViewById(R.id.list_view_files);
        txtEmptyRestore = alertDialog.findViewById(R.id.txt_empty_restore);
        btnRemoveAll = alertDialog.findViewById(R.id.img_btn_remove_all);
        Button btnCancel = alertDialog.findViewById(R.id.btn_cancel_restore);

        ArrayAdapterFiles arrayAdapterFiles;
        File[] files = BackupSort.getListFile(folder);

        // assert
        assert listViewFiles != null;
        assert btnCancel != null;
        assert files != null;

        int color = MyColor.getColorCustom(activity);
        btnCancel.setTextColor(color);

        //RestoreHelper.purgeLogFiles(files);

        listFiles = new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(BackupSort.getListFile(folder)));
        Collections.sort(fileList, new BackupSort.SortFileDate());

        for (File file : fileList) {
            listFiles.add(file.getName());
        }

        checkEmptyListFiles();

        // Устанавливам адаптер
        arrayAdapterFiles = new ArrayAdapterFiles(activity, listFiles, folder);
        listViewFiles.setAdapter(arrayAdapterFiles);

        // Click listView
        listViewFiles.setOnItemClickListener((adapterView, view, position, l) -> {
            BackupDb.restoreDatabase(activity, fileList.get(position).getPath());
            if (cbRestoreNotes != null) {
                cbRestoreNotes.callingRestoreNotes();
            }
            alertDialog.dismiss();
        });

        // Click deleteAll
        btnRemoveAll.setOnClickListener(view -> {
            arrayAdapterFiles.deleteAll();
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

    public interface CallbackRestoreNotes {
        void callingRestoreNotes();
    }
}
