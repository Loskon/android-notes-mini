package com.loskon.noteminimalism3.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.backup.main.BackupLocal;
import com.loskon.noteminimalism3.backup.main.BpCloud;
import com.loskon.noteminimalism3.helper.InternetCheck;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.helper.permissions.PermissionsStorage;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarBackup;

import static com.loskon.noteminimalism3.helper.RequestCode.REQUEST_CODE_PERMISSIONS;
import static com.loskon.noteminimalism3.helper.RequestCode.REQUEST_CODE_SIGN_IN;

public class BackupActivity extends AppCompatActivity {

    private BackupLocal backupLocal;
    private BpCloud bpCloud;
    private InternetCheck internetCheck;

    private BottomAppBar btmAppBarSettings;
    private Button btnBpSd, btnRestoreSd;
    private Button btnBpCloud, btnResetCloud;
    private int btnId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        initialiseWidgets();
        initialiseAdapters();
        setColorItems();
        handlerBottomAppBar();
    }

    private void initialiseWidgets() {
        btmAppBarSettings = findViewById(R.id.btmAppBackup);
        btnBpSd = findViewById(R.id.btn_backup_sd);
        btnRestoreSd = findViewById(R.id.btn_restore_sd);
        btnBpCloud = findViewById(R.id.btn_backup_cloud);
        btnResetCloud = findViewById(R.id.btn_restore_cloud);
    }

    private void initialiseAdapters() {
        backupLocal = new BackupLocal(this);
        bpCloud = new BpCloud(this, btmAppBarSettings);
        internetCheck = new InternetCheck(this);
    }

    private void setColorItems() {
        MyColor.setColorStatBarAndTaskDesc(this);
        MyColor.setNavIconColor(this, btmAppBarSettings);

        int color = MyColor.getColorCustom(this);
        btnBpSd.setBackgroundColor(color);
        btnRestoreSd.setBackgroundColor(color);
        btnBpCloud.setBackgroundColor(color);
        btnResetCloud.setBackgroundColor(color);
    }

    private void handlerBottomAppBar() {
        btmAppBarSettings.setNavigationOnClickListener(v -> MyIntent.goSettingsActivity(this));
    }

    public void onClickBtnSd(View view) {
        btnId = view.getId();
        boolean isPermissions = PermissionsStorage
                .verifyStoragePermissions(this, null, true);
        if (isPermissions) {
            btnSd();
        }
    }

    private void btnSd() {
        if (btnId == R.id.btn_backup_sd) {
            backupLocal.performBackup();
        } else if (btnId == R.id.btn_restore_sd) {
            backupLocal.performRestore();
        }
    }

    public void onClickBtnDrive(View view) {
        btnId = view.getId();
        if (internetCheck.isConnected()) {
            btnCloud();
        } else {
            showSnackbar(MySnackbarBackup.MSG_TEXT_NO_INTERNET);
        }
    }

    private void btnCloud() {
        if (btnId == R.id.btn_backup_cloud) {
            bpCloud.backup();
        } else if (btnId == R.id.btn_restore_cloud) {
            bpCloud.restore();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {

            if (resultCode == RESULT_OK) {
               btnCloud();
            } else {
                showSnackbar(MySnackbarBackup.MSG_TEXT_SIGN_IN_FAIL);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btnSd();
            } else {
                showSnackbar(MySnackbarBackup.MSG_TEXT_NO_PERMISSION);
            }
        }
    }

    private void showSnackbar(String message) {
        MySnackbarBackup.showSnackbar(this, false, message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyIntent.goSettingsActivity(this);
    }
}