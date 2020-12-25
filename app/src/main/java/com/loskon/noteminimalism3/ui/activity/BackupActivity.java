package com.loskon.noteminimalism3.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.db.backup.Permissions;
import com.loskon.noteminimalism3.ui.Helper.ColorHelper;
import com.loskon.noteminimalism3.db.backup.LocalBackupAndRestore;
import com.loskon.noteminimalism3.ui.Helper.SharedPrefHelper;
import com.loskon.noteminimalism3.ui.Helper.ToastHelper;

import static com.loskon.noteminimalism3.ui.Helper.MainHelper.REQUEST_CODE_PERMISSIONS;
import static com.loskon.noteminimalism3.db.backup.Permissions.permissionGranted;

public class BackupActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarSettings;
    private LocalBackupAndRestore localBackupAndRestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        // Меняем цвет статус бара
        ColorHelper.setColorStatBarAndTaskDesc(this);

        btmAppBarSettings = findViewById(R.id.btmAppBarColor3);
        btmAppBarSettings.setNavigationOnClickListener(v -> goMainActivity());

        ColorHelper.setNavigationIconColor(this, btmAppBarSettings);

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

    int id;
    public void onClick(View view) {
        id = view.getId();

        if (id == R.id.btn_backup_sd) {
            // Backup
            Permissions.verifyStoragePermissions(this);
            if (permissionGranted) {
                localBackupAndRestore.performBackup(loadPath());
            }
        } else if (id == R.id.btn_import_sd) {
            // Restore
            Permissions.verifyStoragePermissions(this);
            if (permissionGranted) {
                localBackupAndRestore.performRestore(loadPath());
            }
        } else if (id == R.id.btn_backup_drive) {
            Toast.makeText(this, "button4", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_import_drive) {
            Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
        }
    }


    private String loadPath() { // Path for file and folder
        return SharedPrefHelper.loadString(this,
                "Choose directory", String.valueOf(Environment.getExternalStorageDirectory()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (id == R.id.btn_backup_sd) {
                    localBackupAndRestore.performBackup(loadPath());
                } else if (id == R.id.btn_import_sd) {
                    localBackupAndRestore.performRestore(loadPath());
                }
            } else {
                ToastHelper.showToast(this, getString(R.string.no_permissions));
            }
        }
    }
}