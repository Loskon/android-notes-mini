package com.loskon.noteminimalism3.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.db.backup.BackupPath;
import com.loskon.noteminimalism3.db.backup.BackupPermissions;
import com.loskon.noteminimalism3.db.backup.BackupSnackbar;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.db.backup.BackupLocal;
import com.loskon.noteminimalism3.helper.MyIntent;

import static com.loskon.noteminimalism3.helper.MainHelper.REQUEST_CODE_PERMISSIONS;

public class BackupActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarSettings;
    private Button btnBackupSd, btnResetSd, btnBackupDrive, btnResetDrive;
    private BackupLocal backupLocal;
    private int btnId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        initialiseWidgets();
        setColorItems();
        handler();
    }

    private void initialiseWidgets() {
        btmAppBarSettings = findViewById(R.id.btmAppBarBackup);
        btnBackupSd = findViewById(R.id.btn_backup_sd);
        btnResetSd = findViewById(R.id.btn_backup_restore_sd);
        btnBackupDrive = findViewById(R.id.btn_backup_drive);
        btnResetDrive = findViewById(R.id.btn_backup_restore_drive);

        backupLocal = new BackupLocal(this);
    }

    private void setColorItems() {
        MyColor.setColorStatBarAndTaskDesc(this);
        MyColor.setNavigationIconColor(this, btmAppBarSettings);

        int color = MyColor.getColorCustom(this);
        btnBackupSd.setBackgroundColor(color);
        btnResetSd.setBackgroundColor(color);
        btnBackupDrive.setBackgroundColor(color);
        btnResetDrive.setBackgroundColor(color);
    }

    private void handler() {
        btmAppBarSettings.setNavigationOnClickListener(v -> MyIntent.goSettingsActivity(this));
    }

    public void onClickBtnSd(View view) {
        btnId = view.getId();

        if (BackupPermissions.verifyStoragePermissions(this)) {
            btnSd();
        }
    }

    private void btnSd() {
        if (btnId == R.id.btn_backup_sd) {
            backupLocal.performBackup();
        } else if (btnId == R.id.btn_backup_restore_sd) {
            backupLocal.performRestore();
        }
    }

    public void onClickBtnDrive(View view) {
        btnId = view.getId();

        if (btnId == R.id.btn_backup_drive) {
            Toast.makeText(this, "button4", Toast.LENGTH_SHORT).show();
        } else if (btnId == R.id.btn_backup_restore_drive) {
            Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btnSd();
            } else {
                BackupSnackbar.showSnackbar(this,
                        false, BackupSnackbar.MSG_TEXT_NO_PERMISSION);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyIntent.goSettingsActivity(this);
    }
}