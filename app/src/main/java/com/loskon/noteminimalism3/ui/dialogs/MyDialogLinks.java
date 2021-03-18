package com.loskon.noteminimalism3.ui.dialogs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.activity.NoteActivity;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarNoteMessage;

import java.util.regex.Pattern;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Обработка и определение типов гипперсылок
 */

public class MyDialogLinks {

    private final NoteActivity noteActivity;

    private final String titleLinks;
    private String typeLinks;
    private String titleTextView;
    private AlertDialog alertDialog;
    @SuppressWarnings("FieldCanBeLocal")
    private Button btnOpen, btnCopy;
    private TextView textView;

    public static final String URL_WEB = "WEB";
    public static final String URL_MAIL = "MAIL";
    public static final String URL_PHONE = "PHONE";
    public static final String ERROR = "ERROR";

    public MyDialogLinks(NoteActivity noteActivity, String titleLinks) {
        this.noteActivity = noteActivity;
        this.titleLinks = titleLinks;
    }

    public void call() {
        alertDialog = DialogBuilder.buildDialog(noteActivity, R.layout.dialog_open_link);
        alertDialog.show();

        textView = alertDialog.findViewById(R.id.tv_link_title);
        btnOpen = alertDialog.findViewById(R.id.btn_link_open);
        btnCopy = alertDialog.findViewById(R.id.btn_link_copy);

        typeLinks = typeURL(titleLinks);

        setTitle();

        btnOpen.setOnClickListener(onClickListener);
        btnCopy.setOnClickListener(onClickListener);
    }

    private void setTitle() {
        // Уставка имени ссылки
        switch (typeLinks) {
            case URL_MAIL:
                replaceText("mailto:");
                setTextBtn(R.string.dg_open_link_send);
                break;
            case URL_PHONE:
                replaceText("tel:");
                setTextBtn(R.string.dg_open_link_call);
                break;
            default:
                titleTextView = titleLinks;
                setTextBtn(R.string.dg_open_link_open);
                break;
        }

        textView.setText(titleTextView);
    }

    private void setTextBtn(int textBtn) {
        btnOpen.setText(noteActivity.getString(textBtn));
    }

    private void replaceText(String title) {
        titleTextView = titleLinks.replace(title, "");
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int btnId = view.getId();

            if (btnId == R.id.btn_link_open) {
                startLinks();
            } else if (btnId == R.id.btn_link_copy) {
                copyLinks();
            }

            alertDialog.dismiss();
        }
    };

    private void startLinks() {
        // Открытие ссылок
        switch (typeLinks) {
            case URL_WEB:
                noteActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(titleLinks)));
                break;
            case URL_MAIL:
                noteActivity.startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(titleLinks)));
                break;
            case URL_PHONE:
                noteActivity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(titleLinks)));
                break;
            default:
                noteActivity.getMySnackbarNoteMessage().show(false,
                        MySnackbarNoteMessage.MSG_INVALID_LINK);
                break;
        }
    }

    private void copyLinks() {
        ClipboardManager clipboard = (ClipboardManager) noteActivity.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copy", titleTextView);
        clipboard.setPrimaryClip(clip);
    }

    private String typeURL(String titleLinks) {
        // Определение типа ссылки
        String typeLinks = ERROR;
        String regexWebS = ".*https://.*";
        String regexWeb = ".*http://.*";
        String regexMail = ".*mailto:.*";
        String regexPhone = ".*tel:.*";

        boolean matchesWebS = Pattern.matches(regexWebS, titleLinks);
        boolean matchesWeb = Pattern.matches(regexWeb, titleLinks);
        boolean matchesMail = Pattern.matches(regexMail, titleLinks);
        boolean matchesPhone = Pattern.matches(regexPhone, titleLinks);

        if (matchesWeb | matchesWebS) typeLinks = URL_WEB;
        if (matchesMail) typeLinks = URL_MAIL;
        if (matchesPhone) typeLinks = URL_PHONE;

        return typeLinks;
    }
}
