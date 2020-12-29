package com.loskon.noteminimalism3.helper;

public class ReplaceText {

    public static String replaceForSaveTittle(String titleText) {
        titleText = titleText.replace("/", "_");
        titleText = titleText.replace(".", "_");
        titleText = titleText.replace(":", "-");
        return titleText;
    }
}
