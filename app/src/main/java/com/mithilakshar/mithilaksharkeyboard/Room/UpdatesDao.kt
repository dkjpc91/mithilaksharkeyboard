package com.mithilakshar.mithilaksharkeyboard.Room


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface  UpdatesDao  {

    @Query("SELECT uniqueString FROM files WHERE id = :id")
    fun getUniqueStringById(id: Int): LiveData<String>

    @Query("UPDATE files SET uniqueString = :newUniqueString WHERE id = :id")
    suspend fun updateUniqueStringById(id: Long, newUniqueString: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(Updates: Updates)
    @Update
    suspend fun update(Updates: Updates)

    @Query("SELECT * FROM files")
    suspend fun getallupdates(): List<Updates>

    @Query("SELECT * FROM files WHERE id = :id")
    suspend fun findById(id: Int): Updates

    @Query("SELECT * FROM files WHERE fileName = :fileName")
    suspend fun getfileupdate(fileName: String): List<Updates>

    @Query("UPDATE files SET uniqueString = :newUniqueString WHERE fileName = :fileName")
    suspend fun updateUniqueString(fileName: String, newUniqueString: String)
}