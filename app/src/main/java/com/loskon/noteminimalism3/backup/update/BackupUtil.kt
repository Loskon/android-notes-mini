package com.loskon.noteminimalism3.backup.update

/**
 * Получение пути к сохранению бэкапа и текстового файла
 */

class BackupUtil {
    companion object {

        fun findFullPath(receivedPath: String): String {
            // Дает абсолютный путь от дерева uri
            var path = receivedPath
            val actualPAth: String
            var index = 0

            path = path.substring(5)

            val result = StringBuilder("/storage")

            run {
                var i = 0

                while (i < path.length) {
                    if (path[i] != ':') {
                        result.append(path[i])
                    } else {
                        index = ++i
                        result.append('/')
                        break
                    }

                    i++
                }
            }

            for (i in index until path.length) {
                result.append(path[i])
            }

            actualPAth = if (result.substring(9, 16).equals("primary", ignoreCase = true)) {
                result.substring(0, 8) + "/emulated/0/" + result.substring(17)
            } else {
                result.toString()
            }

            return actualPAth
        }
    }
/*
    fun replace(fileTitle: String): String {
        var fileTitle = fileTitle
        fileTitle = fileTitle.replace("/", "_")
        fileTitle = fileTitle.replace(".", "_")
        fileTitle = fileTitle.replace(":", "-")
        return fileTitle
    }*/
}