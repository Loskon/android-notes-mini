package com.loskon.noteminimalism3.ui.preference;

public class PrefHelper {

    public static int getDateFontSize(int fontSize) {
        if (fontSize < 18) return 12;
        else if (fontSize <= 22) return 14;
        else if (fontSize <= 26) return 16;
        else if (fontSize <= 30) return 18;
        else if (fontSize <= 34) return 20;
        else if (fontSize <= 38) return 22;
        else if (fontSize <= 42) return 24;

        return 0;
    }
}
