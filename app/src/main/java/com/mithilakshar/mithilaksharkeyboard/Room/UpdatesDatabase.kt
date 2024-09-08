package com.mithilakshar.mithilaksharkeyboard.Room



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Database(entities = [Updates::class], version = 1)
abstract class UpdatesDatabase : RoomDatabase() {
    abstract fun UpdatesDao(): UpdatesDao

    companion object {
        @Volatile
        private var INSTANCE: UpdatesDatabase? = null


        fun getDatabase(context: Context): UpdatesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UpdatesDatabase::class.java,
                    "Update_database"
                )

                    .addCallback(UpdatesDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    private class UpdatesDatabaseCallback(private val context: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Pre-populate the database on first creation
            INSTANCE?.let { database ->
                GlobalScope.launch(Dispatchers.IO) {
                    // Use runBlocking to call suspend function prepopulateDatabase
                    runBlocking {
                        database.prepopulateDatabase(database.UpdatesDao())
                    }
                }
            }
        }
    }


    private suspend fun prepopulateDatabase(updatesDao: UpdatesDao) {
        // Insert dummy data here

        val mantra = Updates(id = 1, fileName = "Imageslist.db", uniqueString = "Imageslist")
        val add = Updates(id = 2, fileName = "add.db", uniqueString = "e")

        updatesDao.insert(mantra)
        updatesDao.insert(add)


    }


}