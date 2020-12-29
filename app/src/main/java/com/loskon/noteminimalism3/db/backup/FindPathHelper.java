package com.loskon.noteminimalism3.db.backup;

/**
 * эта функция дает абсолютный путь от дерева uri.
 */

public class FindPathHelper {

    public static String findFullPath(String path) {

        String actualResult;
        int index = 0;

        path = path.substring(5);

        StringBuilder result = new StringBuilder("/storage");

        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) != ':') {
                result.append(path.charAt(i));
            } else {
                index = ++i;
                result.append('/');
                break;
            }
        }

        for (int i = index; i < path.length(); i++) {
            result.append(path.charAt(i));
        }

        if (result.substring(9, 16).equalsIgnoreCase("primary")) {
            actualResult = result.substring(0, 8) + "/emulated/0/" + result.substring(17);
        } else {
            actualResult = result.toString();
        }

        return actualResult;
    }
}
