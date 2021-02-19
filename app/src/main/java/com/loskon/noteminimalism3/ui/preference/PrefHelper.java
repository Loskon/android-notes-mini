package com.loskon.noteminimalism3.ui.preference;

import android.app.Activity;
import android.content.Context;

import androidx.preference.PreferenceViewHolder;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.GetSizeItem;

public class PrefHelper {

    public static int getDateFontSize(int fontSize) {
        if (fontSize < 18) return 12;
        else if (fontSize <= 22) return 14;
        else if (fontSize <= 26) return 16;
        else if (fontSize <= 30) return 18;
        else if (fontSize <= 34) return 20;
        else if (fontSize <= 38) return 22;
        else if (fontSize <= 42) return 24;

        return 0;
    }

    public static String getPrefSummary(Activity activity, String prefString, int prefValue) {
        String string = null;

        if (prefString.equals(activity.getString(R.string.num_of_backup))) {
            string = activity.getString(R.string.num_backup_summary);
        } else if (prefString.equals(activity.getString(R.string.retention_trash_title))) {
            string = activity.getString(R.string.number_of_days);
        }

        string = string +" \u2014 " + prefValue;
        return string;
    }

    public static void setTitleSetting(Context context,
                                       PreferenceViewHolder holder, boolean isPadding) {

        holder.itemView.setMinimumHeight(GetSizeItem.getHeightItem(context));
    }
}
