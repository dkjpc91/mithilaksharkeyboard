import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.Log
import android.media.MediaScannerConnection
import android.os.Environment
import com.mithilakshar.mithilaksharkeyboard.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class RawFileCopier(private val context: Context) {

    interface CopyCallback {
        fun onCopyComplete()
    }

    /**
     * Copies all raw files to the 'test' folder in external storage if any file is missing.
     * If any file is missing, it clears the 'test' folder and recopies all files.
     * When complete, it triggers the provided callback.
     */
    fun copyAllRawFilesToTestFolderIfMissing(callback: CopyCallback? = null) {
        // Ensure external storage is writable
        if (!isExternalStorageWritable()) {
            Log.e("RawFileCopier", "External storage is not writable.")
            return
        }

        // Define the target 'test' folder in external storage
        val testFolder = File(context.getExternalFilesDir(null), "test")

        // Create the 'test' folder if it doesn't exist
        if (!testFolder.exists()) {
            val isCreated = testFolder.mkdirs()
            if (isCreated) {
                Log.d("RawFileCopier", "Test folder created successfully at: ${testFolder.absolutePath}")
            } else {
                Log.e("RawFileCopier", "Failed to create test folder.")
                return
            }
        }

        // Delete all .db files in the test folder before copying new files
        deleteExistingDbFiles(testFolder)

        // Get a list of all raw resources dynamically
        val rawFiles = getAllRawFiles()
        Log.d("RawFileCopier", "Found raw files: ${rawFiles.map { it.second }}")

        // Copy each raw file to the 'test' folder
        rawFiles.forEach { (rawFileId, fileName) ->
            copyRawFileToTestFolder(rawFileId, testFolder, fileName)
        }

        // Verify all files were successfully copied
        val allFilesCopied = rawFiles.all { (_, fileName) ->
            val file = File(testFolder, fileName)
            file.exists() && file.canRead()
        }

        if (allFilesCopied) {
            Log.d("RawFileCopier", "All raw files copied successfully.")
            callback?.onCopyComplete()
        } else {
            Log.e("RawFileCopier", "Some files failed to copy. Callback will not be triggered.")
        }
    }

    /**
     * Deletes all .db files in the specified folder.
     */
    private fun deleteExistingDbFiles(testFolder: File) {
        // Delete all .db files in the test folder
        testFolder.listFiles()?.forEach { file ->
            if (file.name.endsWith(".db")) {
                file.delete()
                Log.d("RawFileCopier", "Deleted existing file: ${file.absolutePath}")
            }
        }
    }

    /**
     * Retrieves a list of all raw resource IDs and their names as filenames.
     */
    private fun getAllRawFiles(): List<Pair<Int, String>> {
        val rawFiles = mutableListOf<Pair<Int, String>>()
        val rawResourceClass = R.raw::class.java

        // Use reflection to get all fields in the R.raw class
        for (field in rawResourceClass.declaredFields) {
            val resourceId = field.getInt(null) // Get resource ID
            val fileName = field.name + ".db"  // Use field name as filename with .db extension
            rawFiles.add(resourceId to fileName)
        }

        return rawFiles
    }

    /**
     * Copies a single raw file to the specified 'test' folder with a specified name.
     */
    private fun copyRawFileToTestFolder(rawFileId: Int, testFolder: File, fileName: String) {
        try {
            val inputStream: InputStream = context.resources.openRawResource(rawFileId)
            val outputFile = File(testFolder, fileName)

            // Copy data from raw file to output file
            Log.d("RawFileCopier", "Copying $fileName to ${outputFile.absolutePath}")
            FileOutputStream(outputFile).use { outputStream ->
                inputStream.copyTo(outputStream)
                outputStream.flush() // Ensure all data is written
            }

            // Check if the file exists and is readable
            if (outputFile.exists() && outputFile.canRead()) {
                Log.d("RawFileCopier", "Successfully copied $fileName to test folder.")

                // Notify media scanner if needed
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(outputFile.absolutePath),
                    null
                ) { path, uri ->
                    Log.d("RawFileCopier", "MediaScanner updated for file: $path")
                }
            } else {
                Log.e("RawFileCopier", "File not accessible immediately after copying: ${outputFile.absolutePath}")
            }

        } catch (e: Exception) {
            Log.e("RawFileCopier", "Failed to copy raw file: $fileName", e)
        }
    }

    /**
     * Check if external storage is writable
     */
    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED
    }

    /**
     * Check if external storage is readable (important for older versions)
     */
    private fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED || state == Environment.MEDIA_MOUNTED_READ_ONLY
    }
}
