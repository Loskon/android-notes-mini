package com.loskon.noteminimalism3.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;

import java.util.Objects;

public class MySettingsAppFragment extends PreferenceFragmentCompat implements
        Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private Activity activity;

    private String switchOneSizeString, resetString, selectColorString;

    private SwitchPreference switchOneSizePref;

    private Preference resetPref, selectColorPref;

    private static CallbackOneSize callbackOneSize;
    private static CallbackReset callbackReset;

    public void registerCallBackOneSize(CallbackOneSize callbackOneSize){
        MySettingsAppFragment.callbackOneSize = callbackOneSize;
    }

    public void registerCallBackReset(CallbackReset callbackReset){
        MySettingsAppFragment.callbackReset = callbackReset;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        (new MyDialogColor()).registerCallBackColorNotifyData(
                () -> Objects.requireNonNull(getListView().getAdapter()).notifyDataSetChanged());

        setDivider(null);
        setDividerHeight(0);

        getListView().setVerticalScrollBarEnabled(false);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_app, rootKey);

        activity = requireActivity();

        switchOneSizeString = getString(R.string.one_size_cards);
        resetString = getString(R.string.reset);
        selectColorString = getString(R.string.select_color);

        switchOneSizePref = findPreference(switchOneSizeString);
        resetPref = findPreference(resetString);
        selectColorPref = findPreference(selectColorString);

        switchOneSizePref.setOnPreferenceChangeListener(this);
        resetPref.setOnPreferenceClickListener(this);
        selectColorPref.setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(resetString)) {
            if (callbackReset != null) {
                callbackReset.callingBackReset();
            }
            return true;
        } else if (key.equals(selectColorString)) {
            MyDialogColor.alertDialogShowColorPicker(activity);
            return true;
        }

        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if (key.equals(switchOneSizeString)) {
            if (callbackOneSize != null) {
                callbackOneSize.callingBackOneSize((Boolean) newValue);
            }
            return true;
        }
        return false;
    }


    public interface CallbackOneSize {
        void callingBackOneSize(boolean isOneSizeOn);
    }

    public interface CallbackReset {
        void callingBackReset();
    }

}