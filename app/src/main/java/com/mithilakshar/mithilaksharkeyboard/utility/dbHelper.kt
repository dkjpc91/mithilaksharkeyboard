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


    @SuppressLint("Range")
    fun getLayoutData(): List<Map<String, Any?>> {
        val layoutList = mutableListOf<Map<String, Any?>>()
        db?.let { database ->
            if (!database.isOpen) {
                Log.w(TAG, "Database not open for reading Layout Data")
                return emptyList()
            }

            val query = "SELECT * FROM layouthelper"
            database.rawQuery(query, null)?.use { cursor ->
                val columnNames = cursor.columnNames // Dynamically fetch column names
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
                    layoutList.add(rowData)
                }
            }
        } ?: Log.e(TAG, "Database is null!")

        return layoutList
    }

    @SuppressLint("Range")
    fun getUniqueRandomRows(): List<Map<String, Any?>> {
        val uniqueRows = mutableListOf<Map<String, Any?>>()
        val groupedData = mutableMapOf<String, MutableList<Map<String, Any?>>>()

        db?.let { database ->
            if (!database.isOpen) {
                Log.w(TAG, "Database not open for reading unique random rows")
                return emptyList()
            }

            val query = "SELECT * FROM layouthelper"
            database.rawQuery(query, null)?.use { cursor ->
                val columnNames = cursor.columnNames // Get all column names dynamically
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

                    // Group by the 'name' column
                    val name = rowData["name"] as? String ?: continue
                    groupedData.getOrPut(name) { mutableListOf() }.add(rowData)
                }
            }
        } ?: Log.e(TAG, "Database is null!")

        // Select a random row for each unique name
        groupedData.forEach { (_, rows) ->
            uniqueRows.add(rows.random()) // Pick a random entry from the group
        }

        return uniqueRows
    }

    @SuppressLint("Range")
    fun getLayoutDataByName(name: String): List<Map<String, Any?>> {
        val layoutList = mutableListOf<Map<String, Any?>>()

        db?.let { database ->
            if (!database.isOpen) {
                Log.w(TAG, "Database not open for reading Layout Data")
                return emptyList()
            }

            val query = "SELECT * FROM layouthelper WHERE name = ?"
            database.rawQuery(query, arrayOf(name)).use { cursor ->
                val columnNames = cursor.columnNames // Get all column names dynamically

                while (cursor.moveToNext()) {
                    val rowData = mutableMapOf<String, Any?>()

                    for (columnName in columnNames) {
                        val columnIndex = cursor.getColumnIndex(columnName)
                        if (columnIndex >= 0) {
                            val value = when (cursor.getType(columnIndex)) {
                                Cursor.FIELD_TYPE_NULL -> null
                                Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(columnIndex)
                                Cursor.FIELD_TYPE_FLOAT -> cursor.getDouble(columnIndex)
                                Cursor.FIELD_TYPE_STRING -> cursor.getString(columnIndex)
                                Cursor.FIELD_TYPE_BLOB -> cursor.getBlob(columnIndex)
                                else -> null
                            }
                            rowData[columnName] = value
                        }
                    }
                    layoutList.add(rowData)
                }
            }
        } ?: Log.e(TAG, "Database is null!")

        return layoutList
    }



    fun closeDatabase() {
        db?.close()
    }
}