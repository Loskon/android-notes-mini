package com.loskon.noteminimalism3.backup.prime;

import android.net.Uri;
import android.os.CountDownTimer;
import android.view.Menu;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.ui.activity.BackupActivity;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogData;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogProgress;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarBackup;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static com.loskon.noteminimalism3.auxiliary.other.RequestCode.REQUEST_CODE_SIGN_IN;
import static com.loskon.noteminimalism3.db.DbHelper.DATABASE_NAME;

/**
 * Аутентификация пользователя, экспорт/импорт копии базы данны в/из облака
 */

public class BpCloud {

    private final BackupActivity activity;
    private MyDialogProgress dialogProgress;

    @SuppressWarnings("FieldCanBeLocal")
    private FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;
    private AuthUI authUI;
    private StorageReference storageRef, dbRef;
    private FirebaseUser user;

    private File dbFile;

    private Menu appBarMenu;
    private BottomAppBar bottomAppBar;

    private UploadTask uploadTask;
    private StorageTask<FileDownloadTask.TaskSnapshot> downloadTask;

    private CountDownTimer countDownTimer;
    private boolean isFinishTimer;

    private static CallbackResNotes cbResNotes;

    public static void regCallbackCloud(CallbackResNotes cbResNotes) {
        BpCloud.cbResNotes = cbResNotes;
    }

    public BpCloud(BackupActivity activity) {
        this.activity = activity;
    }

    public void initialiseSettings() {
        bottomAppBar = activity.getBtmAppBarSettings();

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        authUI = AuthUI.getInstance();

        storageRef = storage.getReference();

        appBarMenu = bottomAppBar.getMenu();
        dbFile = new File(getDbPath());

        dialogProgress = new MyDialogProgress(activity);

        bottomAppBarMenuHandler();
        getUser();
    }

    private String getDbPath() {
        return activity.getDatabasePath(DATABASE_NAME).toString();
    }

    private void bottomAppBarMenuHandler() {
        bottomAppBar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.action_account) {
                (new MyDialogData(activity)).call();
                return true;
            }

            return false;
        });
    }

    private void getUser() {
        user = firebaseAuth.getCurrentUser();
        setMenuItemVisibility();
    }

    private void setMenuItemVisibility() {
        appBarMenu.findItem(R.id.action_account).setVisible(user != null);
        if (user != null) MyColor.setColorMenuItem(activity, appBarMenu);
    }

    public void backupAndRestore(boolean isBackup) {
        getUser();

        if (user != null) {
            callBpAndRe(isBackup);
        } else {
            goSignIn();
        }
    }

    private void callBpAndRe(boolean isBackup) {
        dbRef = storageRef.child(cloudPath());
        callDialogAndTimer();

        if (isBackup) {
            uploadFile();
        } else {
            downloadFile();
        }
    }

    private String cloudPath() {
        return activity.getString(R.string.app_name_backup) +
                File.separator + user.getUid() + File.separator + DATABASE_NAME;
    }

    private void callDialogAndTimer() {
        dialogProgress.call();
        setCountDownTimer();
    }

    private void setCountDownTimer() {
        isFinishTimer = true;

        countDownTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (isFinishTimer) onFinishTimer();
            }
        }.start();
    }

    private void onFinishTimer() {
        if (uploadTask != null) uploadTask.cancel();
        if (downloadTask != null) downloadTask.cancel();
        dialogProgress.close();
        showSnackbar(false, MySnackbarBackup.MSG_INTERNET_PROBLEM);
    }

    private void uploadFile() {
        Uri dbUrl = Uri.fromFile(dbFile);
        uploadTask = dbRef.putFile(dbUrl);
        uploadTask
                .addOnSuccessListener(this::onSuccessBackup)
                .addOnFailureListener(this::onFailureBackup);
    }

    private void downloadFile() {
        downloadTask = dbRef.getFile(dbFile);
        downloadTask
                .addOnSuccessListener(this::onSuccessRestore)
                .addOnFailureListener(this::onFailureRestore);
    }

    private void goSignIn() {
        List<AuthUI.IdpConfig> providers = Collections
                .singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());

        activity.startActivityForResult(
                authUI
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.AuthenticationTheme)
                        .build(),
                REQUEST_CODE_SIGN_IN);
    }

    private void onSuccessBackup(UploadTask.TaskSnapshot taskSnapshot) {
        closeDialogAndTimer();
        showSnackbar(true, MySnackbarBackup.MSG_BACKUP_COMPLETED);
    }

    private void onFailureBackup(Exception exception) {
        exception.printStackTrace();
        closeDialogAndTimer();
        showSnackbar(false, MySnackbarBackup.MSG_BACKUP_FAILED);
    }

    private void onSuccessRestore(FileDownloadTask.TaskSnapshot taskSnapshot) {
        closeDialogAndTimer();
        cbResNotes.onCallBack();
        showSnackbar(true, MySnackbarBackup.MSG_RESTORE_COMPLETED);
    }

    private void onFailureRestore(Exception exception) {
        exception.printStackTrace();
        closeDialogAndTimer();
        showSnackbar(false, MySnackbarBackup.MSG_RESTORE_FAILED);
    }

    private void showSnackbar(boolean isSuccess, String typeMessage) {
        MySnackbarBackup.showSnackbar(activity, isSuccess, typeMessage);
    }

    private void closeDialogAndTimer() {
        isFinishTimer = false;
        dialogProgress.close();
        countDownTimer.cancel();
    }

    public void signOut() {
        firebaseAuth.signOut();
        authUI
                .signOut(activity)
                .addOnCompleteListener(this::onSuccessSignOut);
        getUser();
    }

    private void onSuccessSignOut(Task<Void> voidTask) {
        showSnackbar(true, MySnackbarBackup.MSG_TEXT_OUT);
    }

    public void deleteData() {
        if (dbRef != null) dbRef.delete();
        firebaseAuth.signOut();
        authUI
                .delete(activity)
                .addOnCompleteListener(this::onSuccesDelData);
        authUI.signOut(activity);

        getUser();
    }

    private void onSuccesDelData(Task<Void> voidTask) {
        showSnackbar(true, MySnackbarBackup.MSG_DEL_DATA);
    }

    public interface CallbackResNotes {
        void onCallBack();
    }
}
