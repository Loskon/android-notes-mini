package com.loskon.noteminimalism3.ui.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.auxiliary.other.AppFontManager;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogTypeFont;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogWarning;

/**
 * Класс для выбора шрифта в приложении
 */

public class FontsActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarFonts;
    private RadioGroup radioGroup;
    private Menu appBarMenu;
    private int checkedId;
    private boolean isDialogShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyColor.setDarkTheme(GetSharedPref.isDarkMode(this));
        new AppFontManager(this).setFont();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fonts);
        MyColor.setColorStatBarAndTaskDesc(this);

        initialiseWidgets();
        setColorItems();
        handlerBottomAppBar();

        if (isDialogShow) {
            new MyDialogWarning(this).call();
        }
    }

    private void initialiseWidgets() {
        radioGroup = findViewById(R.id.radioGroupFonts);
        btmAppBarFonts = findViewById(R.id.btmAppFonts);
        appBarMenu = btmAppBarFonts.getMenu();
        isDialogShow = GetSharedPref.isDialogShow(this);
    }

    private void setColorItems() {
        MyColor.setNavIconColor(this, btmAppBarFonts);
        MyColor.setColorMenuItem(this, appBarMenu);
    }

    private void handlerBottomAppBar() {
        btmAppBarFonts.setNavigationOnClickListener(v -> MyIntent.goSettingsActivity(this));

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton childAt = (RadioButton) radioGroup.getChildAt(i);
            childAt.setButtonTintList(ColorStateList.valueOf(MyColor.getMyColor(this)));
        }

        checkedId = GetSharedPref.getTypeFont(this);

        if (checkedId != -1) {
            radioGroup.check(checkedId);
        } else {
            radioGroup.check(R.id.rb_font_default);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> this.checkedId = checkedId);

        btmAppBarFonts.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_select_font) {
                new MyDialogTypeFont(this).call(checkedId);
                return true;
            }

            return false;
        });
    }
}