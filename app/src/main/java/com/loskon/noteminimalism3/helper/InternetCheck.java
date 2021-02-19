package com.loskon.noteminimalism3.helper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

public class InternetCheck  {

    private final Activity activity;


    public InternetCheck(Activity activity) {
        this.activity = activity;
    }

    public boolean isConnected() {
        boolean status = false;

        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null && cm.getActiveNetwork() != null && cm.getNetworkCapabilities(cm.getActiveNetwork()) != null) {
                // connected to the internet
                status = true;
            }
        } else {
            if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
                // connected to the internet
                status = true;
            }
        }

        return status;
    }
}
