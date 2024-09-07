package com.mithilakshar.mithilaksharkeyboard.utility
import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.io.File

class dbHelper(context: Context, dbName: String) {

    private val TAG = "DBHelper"
    val dbFolderPath = context.getExternalFilesDir(null)?.absolutePath + File.separator + "test"
    val dbFilePath = "$dbFolderPath/$dbName"
    private var db: SQLiteDatabase? = null

    init {
        try {
            db = SQLiteDatabase.openDatabase(dbFilePath, null, SQLiteDatabase.OPEN_READWRITE)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening database", e)
        }
    }


    @SuppressLint("Range")
    fun getImageList(): List<Map<String, Any?>> {
        val imageList = mutableListOf<Map<String, Any?>>()
        db?.let { database ->
            if (!database.isOpen) {
                Log.w(TAG, "Database not open for reading ImageList")
                return emptyList()
            }

            val query = "SELECT * FROM Imageslist"
            database.rawQuery(query, null)?.use { cursor ->
                val columnNames = cursor.columnNames  // Get column names from the cursor dynamically
                while (cursor.moveToNext()) {
                    val rowData = mutableMapOf<String, Any?>()
                    for (columnName in columnNames) {
                        val value = when (cursor.getType(cursor.getColumnIndex(columnName))) {
                            Cursor.FIELD_TYPE_NULL -> null
                            Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(cursor.getColumnIndex(columnName))
                            Cursor.FIELD_TYPE_FLOAT -> cursor.getDouble(cursor.getColumnIndex(columnName))
                            Cursor.FIELD_TYPE_STRING -> cursor.getString(cursor.getColumnIndex(columnName))
                            Cursor.FIELD_TYPE_BLOB -> cursor.getBlob(cursor.getColumnIndex(columnName))
                            else -> null
                        }
                        rowData[columnName] = value
                    }
                    imageList.add(rowData)
                }
            }
        } ?: Log.e(TAG, "Database is null!")

        return imageList
    }
}