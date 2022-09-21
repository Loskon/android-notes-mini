package com.loskon.noteminimalism3.utils

object FilePathUtil {

    fun getBackupPathSummary(backupPath: String): String {
        var summary = backupPath
        summary = summary.replace("//", "/")
        summary = summary.replace("storage/", "")
        return summary
    }

    fun getCorrectedTreePath(selectedPath: String): String? {
        var path = selectedPath

        if (path.contains("/tree/home:")) {
            path = path.replace("/tree/home:", "/tree/primary:Documents/")
        }

        if (path.contains("/tree/downloads")) {
            path = path.replace("/tree/downloads", "/tree/primary:Downloads/")
        }

        return if (path.contains("/tree/primary")) {
            path
        } else {
            null
        }
    }

    fun findFullPath(uriPath: String): String? {
        return try {
            val actualPAth: String
            var path = uriPath
            var index = 0
            val result = StringBuilder("/storage")

            path = path.substring(5)

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

            actualPAth
        } catch (exception: Exception) {
            null
        }
    }
}