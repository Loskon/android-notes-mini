package com.loskon.noteminimalism3.auxiliary.other;

/**
 * Замена определенных символов, для создания имени файла
 */

public class ReplaceText {

    public static String replace(String fileTitle) {
        fileTitle = fileTitle.replace("/", "_");
        fileTitle = fileTitle.replace(".", "_");
        fileTitle = fileTitle.replace(":", "-");
        return fileTitle;
    }
}
