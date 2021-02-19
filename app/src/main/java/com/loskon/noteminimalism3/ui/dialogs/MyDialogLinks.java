package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.loskon.noteminimalism3.R;

import java.util.regex.Pattern;

import static android.content.Context.CLIPBOARD_SERVICE;

public class MyDialogLinks {

    private final Activity activity;
    private final String title;
    private String typeURL;
    private String titleText;
    private AlertDialog alertDialog;
    private Button btn1;

    public static final String URL_WEB = "WEB";
    public static final String URL_MAIL = "MAIL";
    public static final String URL_PHONE = "PHONE";

    public MyDialogLinks(Activity activity, String title) {
        this.activity = activity;
        this.title = title;
    }

    public void callDialog() {
        alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_open_link);
        alertDialog.show();

        TextView textView = alertDialog.findViewById(R.id.textView5);
        btn1 = alertDialog.findViewById(R.id.dialog_btn_open);
        Button btn2 = alertDialog.findViewById(R.id.dialog_btn_copy);

        typeURL = typeURL(title);

        setPref();

        textView.setText(titleText);

        btn1.setOnClickListener(onClickListener);
        btn2.setOnClickListener(onClickListener);
    }

    private void setPref() {
        switch (typeURL) {
            case URL_WEB:
                titleText = title;
                setTextBtn(R.string.open);
                break;
            case URL_MAIL:
                replaceText("mailto:");
                setTextBtn(R.string.send);
                break;
            case URL_PHONE:
                replaceText("tel:");
                setTextBtn(R.string.call);
                break;
        }
    }

    private void setTextBtn(int textBtn) {
        btn1.setText(activity.getString(textBtn));
    }

    private void replaceText(String tar) {
        titleText = title.replace(tar, "");
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int btnId = view.getId();

            if (btnId == R.id.dialog_btn_open) {

                switch (typeURL) {
                    case URL_WEB:
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(title)));
                        break;
                    case URL_MAIL:
                        activity.startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(title)));
                        break;
                    case URL_PHONE:
                        activity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(title)));
                        break;
                }

            } else if (btnId == R.id.dialog_btn_copy) {
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", titleText);
                clipboard.setPrimaryClip(clip);
            }

            alertDialog.dismiss();
        }
    };

    private String typeURL(String string) {
        String type = null;
        String regexWeb1 = ".*https://.*";
        String regexWeb = ".*http://.*";
        String regexMail = ".*mailto:.*";
        String regexPhone = ".*tel:.*";

        boolean matchesWeb1 = Pattern.matches(regexWeb1, string);
        boolean matchesWeb = Pattern.matches(regexWeb, string);
        boolean matchesMail = Pattern.matches(regexMail, string);
        boolean matchesPhone = Pattern.matches(regexPhone, string);

        if (matchesWeb | matchesWeb1) type = URL_WEB;
        if (matchesMail) type = URL_MAIL;
        if (matchesPhone) type = URL_PHONE;

        return type;
    }
}
