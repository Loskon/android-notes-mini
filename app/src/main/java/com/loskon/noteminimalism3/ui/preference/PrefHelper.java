package com.loskon.noteminimalism3.ui.preference;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.widget.TextView;

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

    public static String getNum(Activity activity, int numOfBackup) {
        String string = activity.getString(R.string.num_backup_summary);
        string = string +" \u2014 " + numOfBackup;
        return string;
    }

    public static void setItemsSize(Context context, PreferenceViewHolder holder) {
        TextView titleTextView = (TextView) holder.findViewById(android.R.id.title);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        holder.itemView.setMinimumHeight(GetSizeItem.getHeightItem(context));
    }
}
