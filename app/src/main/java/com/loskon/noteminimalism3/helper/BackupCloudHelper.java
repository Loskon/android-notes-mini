package com.loskon.noteminimalism3.helper;

import android.app.Activity;
import android.view.Menu;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;

import java.io.File;

public class BackupCloudHelper {

    private final Activity activity;

    private File dbFile;
    private Menu appBarMenu;
    private BottomAppBar bottomAppBar;


    public BackupCloudHelper (Activity activity, BottomAppBar bottomAppBar) {
        this.activity = activity;
        this.bottomAppBar = bottomAppBar;
        appBarMenu = bottomAppBar.getMenu();
    }

    private void setVisLogMenu(boolean isVis) {
        appBarMenu.findItem(R.id.action_logout).setVisible(isVis);
        MyColor.setColorMenuIcon(activity, appBarMenu);
    }
}
