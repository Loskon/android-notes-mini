package com.loskon.noteminimalism3.ui.Helper;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import java.lang.reflect.Field;

/**
 * Tint the cursor and select handles of an {@link EditText} programmatically.
 */
public class EditTextTint {

    public static void setCursorDrawableColor(EditText editText, int color) {
        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);

            // Left
            Field fCursorDrawableLeftRes = TextView.class.getDeclaredField("mTextSelectHandleLeftRes");
            fCursorDrawableLeftRes.setAccessible(true);
            int mCursorDrawableLeftRes = fCursorDrawableLeftRes.getInt(editText);

            // Right
            Field fCursorDrawableRightRes = TextView.class.getDeclaredField("mTextSelectHandleRightRes");
            fCursorDrawableRightRes.setAccessible(true);
            int mCursorDrawableRightRes = fCursorDrawableRightRes.getInt(editText);

            // Right
            Field fCursorDrawableCenterRes = TextView.class.getDeclaredField("mTextSelectHandleRes");
            fCursorDrawableCenterRes.setAccessible(true);
            int mCursorDrawableCenterRes = fCursorDrawableCenterRes.getInt(editText);

            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);

            Drawable[] drawables = new Drawable[4];
            Resources res = editText.getContext().getResources();
            drawables[0] = res.getDrawable(mCursorDrawableRes);
            drawables[1] = res.getDrawable(mCursorDrawableLeftRes);
            drawables[2] = res.getDrawable(mCursorDrawableRightRes);
            drawables[3] = res.getDrawable(mCursorDrawableCenterRes);
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawables[2].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawables[3].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            fCursorDrawable.set(editor, drawables);
        } catch (final Throwable ignored) {}
    }}