package com.loskon.noteminimalism3.backup.main;

import android.app.Activity;
import android.net.Uri;
import android.view.Menu;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogProgress;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarBackup;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static com.loskon.noteminimalism3.db.DbHelper.DATABASE_NAME;
import static com.loskon.noteminimalism3.helper.RequestCode.REQUEST_CODE_SIGN_IN;

/**
 * Аутентификация пользователя, экспорт/импорт копии базы данны в/из облака
 */

public class BpCloud {

    private final Activity activity;
    private MyDialogProgress myDialogProgress;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private FirebaseUser user;
    private AuthUI authUI;
    private StorageReference storageRef, dbRef;

    private File dbFile;

    private Menu appBarMenu;
    private final BottomAppBar bottomAppBar;

    private static CallbackResNotes cbResNotes;

    public static void regCallbackRestNotes(CallbackResNotes cbResNotes) {
        BpCloud.cbResNotes = cbResNotes;
    }

    public BpCloud(Activity activity, BottomAppBar bottomAppBar) {
        this.activity = activity;
        this.bottomAppBar = bottomAppBar;
        loadSettings();
    }

    private void loadSettings() {
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        authUI = AuthUI.getInstance();

        storageRef = storage.getReference();

        appBarMenu = bottomAppBar.getMenu();
        dbFile = new File(dbPath());

        myDialogProgress = new MyDialogProgress(activity);

        bottomAppBarMenuHandler();

        getUser();
    }

    private void bottomAppBarMenuHandler() {
        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                signOut();
                return true;
            } else
                return false;
        });
    }

    private void setVisLogMenu() {
        appBarMenu.findItem(R.id.action_logout).setVisible(user != null);
        MyColor.setColorMenuIcon(activity, appBarMenu);
    }

    private void getAndSetCloudPath() {
        dbRef = storageRef.child(cloudPath());
        myDialogProgress.call();
    }

    private void getUser() {
        user = firebaseAuth.getCurrentUser();
        setVisLogMenu();
    }

    public void backup() {
        getUser();
        if (user != null) {
            getAndSetCloudPath();
            Uri dbUrl = Uri.fromFile(dbFile);
            dbRef.putFile(dbUrl).addOnSuccessListener(taskSnapshot -> {
                showSnackbar(true, MySnackbarBackup.MSG_BACKUP_COMPLETED);
            }).addOnFailureListener(exception -> {
                showSnackbar(false, MySnackbarBackup.MSG_BACKUP_NO_COMPLETED);
            });
        } else {
            goSignIn();
        }
    }

    private void showSnackbar(boolean isSuccess, String typeMessage) {
        myDialogProgress.close();
        MySnackbarBackup.showSnackbar(activity, isSuccess, typeMessage);
    }

    private String cloudPath() {
        return activity.getString(R.string.app_name_backup) +
                File.separator + user.getUid() + File.separator + DATABASE_NAME;
    }

    private String dbPath() {
        return activity.getDatabasePath(DATABASE_NAME).toString();
    }

    public void restore() {
        getUser();
        if (user != null) {
            getAndSetCloudPath();
            dbRef.getFile(dbFile).addOnSuccessListener(taskSnapshot -> {
                cbResNotes.callingResNotes();
                showSnackbar(true, MySnackbarBackup.MSG_RESTORE_COMPLETED);
            }).addOnFailureListener(exception -> {
                showSnackbar(false, MySnackbarBackup.MSG_RESTORE_NO_COMPLETED);
            });
        } else {
            goSignIn();
        }
    }

    public void goSignIn() {
        List<AuthUI.IdpConfig> providers = Collections
                .singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());

        activity.startActivityForResult(
                authUI
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.GreenTheme)
                        .build(),
                REQUEST_CODE_SIGN_IN);

    }

    private void signOut() {
        firebaseAuth.signOut();
        authUI
                .signOut(activity)
                .addOnCompleteListener(task ->
                        MySnackbarBackup.showSnackbar(activity, true, MySnackbarBackup.MSG_TEXT_OUT));
        getUser();
    }

    public interface CallbackResNotes {
        void callingResNotes();
    }
}
