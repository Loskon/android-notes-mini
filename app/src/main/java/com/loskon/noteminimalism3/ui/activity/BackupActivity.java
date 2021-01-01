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
import com.loskon.noteminimalism3.db.backup.MyPath;
import com.loskon.noteminimalism3.db.backup.Permissions;
import com.loskon.noteminimalism3.db.backup.SnackbarBackup;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.db.backup.LocalBackupAndRestore;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.helper.MySnackbar;
import com.loskon.noteminimalism3.helper.MyToast;

import static com.loskon.noteminimalism3.helper.MainHelper.REQUEST_CODE_PERMISSIONS;

public class BackupActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarSettings;
    private Button btnBackupSd, btnResetSd, btnBackupDrive, btnResetDrive;
    private LocalBackupAndRestore localBackupAndRestore;
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

        localBackupAndRestore = new LocalBackupAndRestore(this);
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

        if (Permissions.verifyStoragePermissions(this)) {
            btnSd();
        }
    }

    private void btnSd() {
        String path = MyPath.loadPath(this);

        if (btnId == R.id.btn_backup_sd) {
            localBackupAndRestore.performBackup(path);
        } else if (btnId == R.id.btn_backup_restore_sd) {
            localBackupAndRestore.performRestore(path);
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
                SnackbarBackup.showSnackbar(this,
                        false, SnackbarBackup.MSG_TEXT_NO_PERMISSION);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyIntent.goSettingsActivity(this);
    }
}