package com.loskon.noteminimalism3.ui.Helper;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.activity.NoteActivity;

public class MainHelper {

    public static final int REQUEST_CODE_PERMISSIONS = 298;

    public static Intent intentCreateNewNote(Context context, int selectedNoteMode) {
        // Переход в создание/редактирование заметки
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra("selectedNoteMode", selectedNoteMode);
        return intent;
    }

    public static void bottomNavViewShow(FragmentManager fragmentManager) {
        if (fragmentManager.findFragmentByTag(BottomSheetDialog.TAG) == null) {
            BottomSheetDialog bottomSheet = BottomSheetDialog.newInstance();
            bottomSheet.show(fragmentManager, BottomSheetDialog.TAG);
        }
    }

    public static void removeFlicker(RecyclerView recyclerView) {
        // Удаляет мерцание элементов
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }

    public static void setMenuIcon(Context context, Menu menu, int id,  int icon) {
        if (menu != null) {
            menu.findItem(id).
                    setIcon(ResourcesCompat.getDrawable(context.getResources(),
                            icon, null));
        }
    }

    public static void intentOpenNote(Context context, int selNotesCategory, long id) {
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra("selectedNoteMode", selNotesCategory);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
}


