package com.loskon.noteminimalism3.others;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.loskon.noteminimalism3.R;

public final class CustomSnackbar
        extends BaseTransientBottomBar<CustomSnackbar> {


    protected CustomSnackbar(@NonNull ViewGroup parent, @NonNull View content, @NonNull ContentViewCallback contentViewCallback) {
        super(parent, content, contentViewCallback);
    }

    public static CustomSnackbar make(ViewGroup parent, int duration) {
        // inflate custom layout
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_snackbar, parent, false);

        // create with custom view
        ContentViewCallback callback = new ContentViewCallback(view);
        CustomSnackbar customSnackbar = new CustomSnackbar(parent, view, callback);
        customSnackbar.setDuration(duration);

        return customSnackbar;
    }


    private static class ContentViewCallback
            implements BaseTransientBottomBar.ContentViewCallback {

        // view inflated from custom layout
        private View view;

        public ContentViewCallback(View view) {
            this.view = view;
        }

        @Override
        public void animateContentIn(int delay, int duration) {// TODO: handle enter animation
        }

        @Override
        public void animateContentOut(int delay, int duration) {// TODO: handle exit animation}
        }
    }
}