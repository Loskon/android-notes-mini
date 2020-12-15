package com.loskon.noteminimalism3.ui.mainHelper;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.loskon.noteminimalism3.ui.activity.NoteActivity;

public class MainHelper {

    private static int selNotesCategory = 0;

    public static int getSelNotesCategory(){
        return MainHelper.selNotesCategory;
    }

    // Если вы не хотите изменять переменную когда-либо, не включайте это
    public static void setSelNotesCategory(int selNotesCategory){
        MainHelper.selNotesCategory = selNotesCategory;
    }

    public static Intent newIntent(Context context, int selectedNoteMode) {
        // Переход в создание/редактирование заметки
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra("selectedNoteMode", selectedNoteMode);
        return intent;
    }

    public static void bottomNavViewShow(FragmentManager fragmentManager) {
        // Вызывавет нижнее меню
        //Когда мы удаляем фрагмент и не добавляем транзакцию в BackStack, то фрагмент уничтожается.
        // Если же транзакция добавляется в BackStack, то, при удалении, фрагмент
        // не уничтожается (onDestroy не вызывается), а останавливается (onStop).

        //FragmentTransaction ft = fragmentManager.beginTransaction();
       // ft.addToBackStack(BottomSheetDialog.TAG);
        //ft.commit();

        BottomSheetDialog bottomSheet =
                BottomSheetDialog.newInstance();
        bottomSheet.show(fragmentManager,
                BottomSheetDialog.TAG);

    }

    public static void removeFlicker(RecyclerView recyclerView) {
        // Удаляет мерцание элементов
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }
}


