package com.loskon.noteminimalism3.preference.item;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.loskon.noteminimalism3.R;

import static android.content.Context.MODE_PRIVATE;

public class PrefItemFontSize extends Preference {

    private SharedPreferences sharedPref;
    private int fontSize;

    private ExternalListener mExternalListener;

    private Context mContext;

    /** Sets an external listener for this preference*/
    public void setExternalListener(ExternalListener listener) {
        mExternalListener = listener;
    }

    // создаем колбек и его метод
    public interface ExternalListener {
        void onPreferenceClick();
    }



    public PrefItemFontSize(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public PrefItemFontSize(Context context) {
        super(context);
        mContext = context;
    }

    public PrefItemFontSize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setLayoutResource(R.layout.pref_font_size);
        //setWidgetLayoutResource(R.layout.preference_theme);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false); // disable parent click
        SeekBar seekBar = (SeekBar) holder.findViewById(R.id.seekBar4);
        seekBar.setClickable(true); // enable custom view click

        sharedPref = getContext().getSharedPreferences("saveFontSize", MODE_PRIVATE);
        fontSize = sharedPref.getInt("fontSize", 18);

        seekBar.setProgress(fontSize - 14);

        // Установка макисмального значения (14+26=40)
        seekBar.setMax(26);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Установка минимального значения 14
                fontSize = progress + 14;
                saveFontSize();


                if (mExternalListener != null) mExternalListener.onPreferenceClick();

            }
            public void onStartTrackingTouch(SeekBar seekBar) { }
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        // the rest of the click binding
    }

    private void saveFontSize() {
        sharedPref = getContext().getSharedPreferences("saveFontSize", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putInt("fontSize", fontSize);
        edit.apply();
    }

}