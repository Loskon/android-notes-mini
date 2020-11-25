package com.loskon.noteminimalism3;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.preference.DialogPreference;

// Сохранение, получение, изменение данных

public class CustomPreference extends DialogPreference {

    private String color;

    public CustomPreference(Context context) {
        // Делегировать другому конструктору
        this(context, null);
    }

    public CustomPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.preferenceStyle);
    }

    public CustomPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        // Делегировать другому конструктору
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public CustomPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setPositiveButtonText(R.string.ok);
        setNegativeButtonText(R.string.cancel);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        // сохраняем в SharedPreference
        persistString(color);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.pref_dialog_color_picker;
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        String string;
        if (defaultValue == null) {
            string = getPersistedString("-16711872");
        } else {
            string = defaultValue.toString();
        }
        setColor(string);
    }

    //    @Override
    //    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
    //        String string;
    //        if (restoreValue) {
    //            if (defaultValue == null) {
    //                string = getPersistedString("-16711872");
    //            } else {
    //                string = getPersistedString(defaultValue.toString());
    //            }
    //        } else {
    //            string = defaultValue.toString();
    //        }
    //        setColor(string);
    //    }

    public static int getColor(String color) {
        return Integer.parseInt(color);
    }
}