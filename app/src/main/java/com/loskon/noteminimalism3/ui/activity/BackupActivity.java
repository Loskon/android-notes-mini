package com.loskon.noteminimalism3.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.mainHelper.ColorHelper;
import com.loskon.noteminimalism3.db.backup.LocalBackupAndRestore;

import java.io.File;

public class BackupActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarSettings;
    private LocalBackupAndRestore localBackupAndRestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        // Меняем цвет статус бара
        ColorHelper.setColorStatBarAndNavView(this);

        btmAppBarSettings = findViewById(R.id.btmAppBarColor3);
        btmAppBarSettings.setNavigationOnClickListener(v -> goMainActivity());

        localBackupAndRestore = new LocalBackupAndRestore(this);
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        goMainActivity();
    }

    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_backup_sd) {
            String outFileName = Environment.getExternalStorageDirectory() +
            File.separator + getResources().getString(R.string.app_name) + File.separator;
            localBackupAndRestore.performBackup(outFileName);
        } else if (id == R.id.btn_import_sd) {
            localBackupAndRestore.performRestore();
        } else if (id == R.id.btn_backup_drive) {
            Toast.makeText(this, "button4", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_import_drive) {
            Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
        }
    }

}