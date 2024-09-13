package com.example.mymusicapp.data

import android.content.Context
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object DatabaseUtils {

    private const val DATABASE_NAME = "music_app1.db"
    private const val DATABASE_PATH = "/data/data/com.example.mymusicapp/databases/"

    fun copyDatabaseIfNeeded(context: Context) {
        val dbPath = DATABASE_PATH + DATABASE_NAME

        // Check if the database already exists in the internal storage
        val dbFile = context.getDatabasePath(DATABASE_NAME)
//        if (dbFile.exists()) {
//            return  // Database already exists, no need to copy
//        }

        // Create the databases folder if it doesn't exist
        dbFile.parentFile?.mkdirs()

        try {
            // Open your local db as the input stream
            val inputStream: InputStream = context.assets.open(DATABASE_NAME)
            // Open the empty db as the output stream
            val outputStream: OutputStream = FileOutputStream(dbPath)

            // Transfer bytes from the input file to the output file
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            // Close the streams
            outputStream.flush()
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
