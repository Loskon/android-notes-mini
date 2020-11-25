package com.loskon.noteminimalism3.others;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.loskon.noteminimalism3.R;

public class PreferenceTheme extends Preference {

    public PreferenceTheme(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreferenceTheme(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.preference_theme);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false); // disable parent click
        View button = holder.findViewById(R.id.theme_dark);
        button.setClickable(true); // enable custom view click
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getContext(),
                        "Пора покормить кота!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        // the rest of the click binding
    }
}