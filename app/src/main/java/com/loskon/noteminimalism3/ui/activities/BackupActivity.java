package com.loskon.noteminimalism3.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.bp.InternetCheck;
import com.loskon.noteminimalism3.auxiliary.other.AppFontManager;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.permissions.PermissionsInterface;
import com.loskon.noteminimalism3.permissions.PermissionsStorageUpdate;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.backup.prime.BackupLocal;
import com.loskon.noteminimalism3.backup.prime.BpCloud;
import com.loskon.noteminimalism3.ui.sheets.SheetPrefConfirm;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarBackup;

import static com.loskon.noteminimalism3.auxiliary.other.RequestCode.REQUEST_CODE_SIGN_IN;

public class BackupActivity extends AppCompatActivity implements PermissionsInterface {

    private BackupLocal backupLocal;
    private BpCloud bpCloud;
    private InternetCheck internetCheck;

    private BottomAppBar btmAppBarSettings;
    private Button btnBpSd, btnRestoreSd;
    private Button btnBpCloud, btnResetCloud;
    private int btnId;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyColor.setDarkTheme(GetSharedPref.isDarkMode(this));
        AppFontManager.setFont(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        MyColor.setColorStatBarAndTaskDesc(this);

        PermissionsStorageUpdate.installingVerification(this, this);

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
        bpCloud = new BpCloud(this);
        internetCheck = new InternetCheck(this);

        bpCloud.initialiseSettings();
    }

    private void setColorItems() {
        MyColor.setNavIconColor(this, btmAppBarSettings);

        int color = MyColor.getMyColor(this);
        btnBpSd.setBackgroundColor(color);
        btnRestoreSd.setBackgroundColor(color);
        btnBpCloud.setBackgroundColor(color);
        btnResetCloud.setBackgroundColor(color);
    }

    private void handlerBottomAppBar() {
        btmAppBarSettings.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void onClickBtnSd(View view) {
        btnId = view.getId();

        boolean isAccess = PermissionsStorageUpdate.hasAccessStorageRequest(this);

        if (isAccess) {
            btnClick();
        }
    }

    public void onClickBtnDrive(View view) {
        btnId = view.getId();
        if (internetCheck.isConnected()) {
            btnClick();
        } else {
            showSnackbar(MySnackbarBackup.MSG_TEXT_NO_INTERNET);
        }
    }

    private void btnClick() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) return;

        if (btnId == R.id.btn_backup_sd) {
            backupLocal.performBackup();
        } else if (btnId == R.id.btn_restore_sd) {
            backupLocal.performRestore();
        } else if (btnId == R.id.btn_backup_cloud) {
            (new SheetPrefConfirm(this)).build(true);
        } else if (btnId == R.id.btn_restore_cloud) {
            (new SheetPrefConfirm(this)).build(false);
        }

        lastClickTime = SystemClock.elapsedRealtime();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                goBackup();
            } else {
                showSnackbar(MySnackbarBackup.MSG_TEXT_SIGN_IN_FAILED);
            }
        }
    }

    @Override
    public void onRequestPermissionsStorageResult(boolean isGranted) {
        if (isGranted) {
            btnClick();
        } else {
            showSnackbar(MySnackbarBackup.MSG_TEXT_NO_PERMISSION);
        }
    }

    private void goBackup() {
        if (btnId == R.id.btn_backup_cloud) {
            bpCloud.backupAndRestore(true);
        } else if (btnId == R.id.btn_restore_cloud) {
            bpCloud.backupAndRestore(false);
        }
    }

    private void showSnackbar(String message) {
        MySnackbarBackup.showSnackbar(this, false, message);
    }

    public BpCloud getBpCloud() {
        return bpCloud;
    }

    public InternetCheck getInternetCheck() {
        return internetCheck;
    }

    public BottomAppBar getBtmAppBarSettings() {
        return btmAppBarSettings;
    }
}