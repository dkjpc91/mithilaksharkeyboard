package com.mithilakshar.mithilaksharkeyboard.utility

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mithilakshar.mithilaksharkeyboard.Room.UpdatesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

class UpdateChecker(private val updatesDao: UpdatesDao, private val context: Context) {

    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("SQLdb")
    private val documentRef = collectionRef.document("posterlist")

    suspend fun getUpdateStatus(): Map<String, String> {
        return withContext(Dispatchers.IO) {
            // Define the folder path
            val dbFolderPath = context.getExternalFilesDir(null)?.absolutePath + File.separator + "test"
            val folder = File(dbFolderPath)

            // Check if the folder exists, if not create it
            if (!folder.exists()) {
                folder.mkdirs()
            }

            // Check if the file 'posterlist' exists in the folder
            val posterlistFile = File(folder, "posterlist.db")
            val document = documentRef.get().await()  // Fetch Firestore document once

            if (document != null) {
                val action = document.getString("action") ?: "delete"
                val url = document.getString("url") ?: ""

                Log.d("UpdateChecker", "Fetched action: $action")
                Log.d("UpdateChecker", "Fetched URL: $url")

                // Check if the file exists
                if (posterlistFile.exists()) {
                    // If the file exists, compare the local database 'uniqueString' with Firestore's action
                    val updates = updatesDao.getfileupdate("posterlist.db")
                    Log.d("UpdateChecker", "Current DB update: $updates")

                    if (updates.isNotEmpty() && updates[0].uniqueString == action) {
                        // If they match, no update is needed
                        Log.d("UpdateChecker", "No update required.")
                        mapOf("action" to "z", "url" to "")
                    } else {
                        // If they don't match, update the local DB with the new action
                        val masterUpdate = updatesDao.findById(3) // Use ID 3
                        masterUpdate?.let {
                            Log.d("UpdateChecker", "Updating record with ID 3: $it")
                            it.uniqueString = action
                            updatesDao.update(it)

                            // After update, confirm the record is updated
                            val updatedRecord = updatesDao.findById(3)
                            Log.d("UpdateChecker", "Updated DB record: $updatedRecord")
                        }

                        // Return the action and URL after the update
                        Log.d("UpdateChecker", "Update required. Action and URL returned.")
                        mapOf("action" to action, "url" to url)
                    }
                } else {
                    // If the file doesn't exist, update is required (no file to check)
                    Log.d("UpdateChecker", "File 'posterlist.db' not found, performing direct update.")

                    // Direct update the local DB with the new action
                    val masterUpdate = updatesDao.findById(3) // Use ID 3
                    masterUpdate?.let {
                        Log.d("UpdateChecker", "Updating record with ID 3: $it")
                        it.uniqueString = action
                        updatesDao.update(it)

                        // After update, confirm the record is updated
                        val updatedRecord = updatesDao.findById(3)
                        Log.d("UpdateChecker", "Updated DB record: $updatedRecord")
                    }

                    // Return the action and URL after the update
                    Log.d("UpdateChecker", "Direct update performed. Action and URL returned.")
                    mapOf("action" to action, "url" to url)
                }
            } else {
                // If document is not found in Firestore, assume update is required
                Log.d("UpdateChecker", "Document not found, assuming update is required.")
                mapOf("action" to "delete", "url" to "")
            }
        }
    }
}
