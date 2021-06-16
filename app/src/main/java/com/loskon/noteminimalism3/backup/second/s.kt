package com.loskon.noteminimalism3.backup.second

import com.loskon.noteminimalism3.room.AppRepository
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class DDDDD {
    companion object {
        @JvmStatic
        fun exportDatabase(inFileName :String) {
                val currentDBPath =
                    AppRepository.getAppDatabase()?.openHelper?.writableDatabase?.path

                val currentDB = File(currentDBPath!!)
                val backupDB = File(inFileName)
                if (currentDB.exists()) {
                    try {
                        val src = FileInputStream(currentDB).channel
                        val dst = FileOutputStream(backupDB).channel
                        dst.transferFrom(src, 0, src.size())
                        src.close()
                        dst.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

            }
        }
    }

}