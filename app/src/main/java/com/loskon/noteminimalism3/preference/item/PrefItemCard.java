package com.loskon.noteminimalism3.preference.item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Xml;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.activity.MainActivity;

import org.xmlpull.v1.XmlPullParser;

public class PrefItemCard extends Preference {

    private TextView textView;
    private Context mContext;
    private AttributeSet attributes;
    private int defStyleAttr2;
    private PrefItemFontSize prefItemFontSize;

    public PrefItemCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrefItemCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.pref_item_card);
        mContext = context;
        attributes = attrs;
        defStyleAttr2 = defStyleAttr;
         Resources resources = getContext().getResources();
         @SuppressLint("ResourceType") XmlPullParser parser = resources.getXml(R.layout.pref_font_size);
        AttributeSet attributes34 = Xml.asAttributeSet(parser);
        prefItemFontSize = new PrefItemFontSize(mContext);
        //setWidgetLayoutResource(R.layout.preference_theme);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false); // disable parent click
        textView = (TextView) holder.findViewById(R.id.title_item_note2);

       // Resources resources = getContext().getResources();
       // @SuppressLint("ResourceType") XmlPullParser parser = resources.getXml(R.layout.pref_font_size);
        //AttributeSet attributes = Xml.asAttributeSet(parser);

        //prefItemFontSize.registerCallBack(this);
        //prefItemFontSize.setExternalListener(() -> Toast.makeText(getContext(),
                    // "You clicked the preference without changing its value222", Toast.LENGTH_LONG).show());
        //PrefItemFontSize toggle = new PrefItemFontSize(mContext);
        //toggle.setExternalListener(() -> Toast.makeText(getContext(),
         //       "You clicked the preference without changing its value2", Toast.LENGTH_LONG).show());
       // toggle.registerCallBack(this);

        //View button = holder.findViewById(R.id.theme_dark);
        //button.setClickable(true); // enable custom view click

        // the rest of the click binding
    }
}