package com.loskon.noteminimalism3.helper;

import android.app.Activity;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.button.MaterialButton;
import com.loskon.noteminimalism3.R;

public class NoteHelper {

    public static void favoriteStatus(Activity activity,
                                      boolean isFavItem, MaterialButton button) {
        if (isFavItem) {
            button.setIcon(ResourcesCompat.getDrawable(activity.getResources(),
                    R.drawable.baseline_star_black_24, null));
        } else {
            button.setIcon(ResourcesCompat.getDrawable(activity.getResources(),
                    R.drawable.baseline_star_border_black_24, null));
        }
    }
}
