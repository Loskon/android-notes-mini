package com.loskon.noteminimalism3.auxiliary.bp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

/**
 * Проверка наличия интернет-соединения
 */

public class InternetCheck  {

    private final Activity activity;

    public InternetCheck(Activity activity) {
        this.activity = activity;
    }

    public boolean isConnected() {
        boolean status = false;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (connectivityManager != null && connectivityManager
                    .getActiveNetwork() != null && connectivityManager
                    .getNetworkCapabilities(connectivityManager.getActiveNetwork()) != null) {
                status = true;
            }
        } else {
            if (connectivityManager != null &&
                    connectivityManager.getActiveNetworkInfo() != null &&
                    connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()) {
                status = true;
            }
        }

        return status;
    }
}
