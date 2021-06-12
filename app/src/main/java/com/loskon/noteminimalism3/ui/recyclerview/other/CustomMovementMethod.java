package com.loskon.noteminimalism3.ui.recyclerview.other;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;


/**
 * Кастомный класс для поиска и обработки нажатия по гиперссылкам
 */

public abstract class CustomMovementMethod extends LinkMovementMethod {

    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if (link.length != 0) {
                onLinkClick(link[0].getURL());
            } else {
                onEmptyClick();
            }

            return true;
        }
        return false;
    }

    abstract public void onLinkClick(String url);
    abstract public void onEmptyClick();
}
