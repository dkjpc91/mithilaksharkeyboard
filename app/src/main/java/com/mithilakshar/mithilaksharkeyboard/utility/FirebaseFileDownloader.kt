package com.mithilakshar.mithilaksharkeyboard.utility

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class FirebaseFileDownloader(private val context: Context) {

    private val TAG = "FirebaseFileDownloader"
    val downloadProgressLiveData: MutableLiveData<Int> = MutableLiveData()

    /**
     * Start downloading given url and filename
     * Callback gives downloaded File or null if error
     */
    fun download(urlString: String, fileName: String, callback: (File?) -> Unit) {
        // Start coroutine internally
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dbFolderPath = context.getExternalFilesDir(null)?.absolutePath + File.separator + "test"
                val folder = File(dbFolderPath)
                if (!folder.exists()) {
                    folder.mkdirs()
                }
                val localFile = File(folder, fileName)

                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP ${connection.responseCode}")
                    withContext(Dispatchers.Main) { callback(null) }
                    return@launch
                }

                val inputStream = connection.inputStream
                val totalSize = connection.contentLength
                var downloadedSize = 0

                val outputStream = FileOutputStream(localFile)
                val buffer = ByteArray(4096)
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    downloadedSize += bytesRead

                    val progress = if (totalSize > 0) (downloadedSize * 100) / totalSize else -1
                    downloadProgressLiveData.postValue(progress)
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()
                connection.disconnect()

                withContext(Dispatchers.Main) { callback(localFile) }
            } catch (e: Exception) {
                Log.e(TAG, "Download error", e)
                withContext(Dispatchers.Main) { callback(null) }
            }
        }
    }
}
