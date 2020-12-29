package com.loskon.noteminimalism3.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.db.backup.Permissions;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.db.backup.LocalBackupAndRestore;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPrefKeys;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPreference;
import com.loskon.noteminimalism3.helper.BuildToast;

import static com.loskon.noteminimalism3.helper.MainHelper.REQUEST_CODE_PERMISSIONS;

public class BackupActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarSettings;
    private LocalBackupAndRestore localBackupAndRestore;
    private int btnId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        // Меняем цвет статус бара
        MyColor.setColorStatBarAndTaskDesc(this);

        btmAppBarSettings = findViewById(R.id.btmAppBarColor3);
        btmAppBarSettings.setNavigationOnClickListener(v -> MyIntent.goSettingsActivity(this));

        MyColor.setNavigationIconColor(this, btmAppBarSettings);

        localBackupAndRestore = new LocalBackupAndRestore(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyIntent.goSettingsActivity(this);
    }


    public void onClick(View view) {
        btnId = view.getId();

        if (Permissions.verifyStoragePermissions(this)) {
            if (btnId == R.id.btn_backup_sd) {
                localBackupAndRestore.performBackup(loadPath());
            } else if (btnId == R.id.btn_import_sd) {
                localBackupAndRestore.performRestore(loadPath());
            }
        }
    }

    public void onClick2(View view) {
        btnId = view.getId();

        if (btnId == R.id.btn_backup_drive) {
            Toast.makeText(this, "button4", Toast.LENGTH_SHORT).show();
        } else if (btnId == R.id.btn_import_drive) {
            Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
        }
    }

    private String loadPath() { // Path for file and folder
        return MySharedPreference.loadString(this,
                MySharedPrefKeys.KEY_SEL_DIRECTORY, String.valueOf(Environment.getExternalStorageDirectory()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (btnId == R.id.btn_backup_sd) {
                    localBackupAndRestore.performBackup(loadPath());
                } else if (btnId == R.id.btn_import_sd) {
                    localBackupAndRestore.performRestore(loadPath());
                }
            } else {
                BuildToast.showToast(this, getString(R.string.no_permissions));
            }
        }
    }
}