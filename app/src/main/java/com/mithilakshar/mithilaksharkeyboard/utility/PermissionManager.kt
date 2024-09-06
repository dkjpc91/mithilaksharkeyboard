import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager(private val context: Context) {

    private val STORAGE_PERMISSION_CODE = 100
    private var permissionGrantedCallback: (() -> Unit)? = null



    fun checkStoragePermission(onPermissionGranted: () -> Unit) {
        this.permissionGrantedCallback = onPermissionGranted

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED
        ) {
            // If permission was denied previously, explain why the app needs it.
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show explanation and re-prompt
                showPermissionExplanationDialog()
            } else {
                // Request permission directly (first time or "Don't ask again" was not selected)
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    STORAGE_PERMISSION_CODE
                )
            }
        } else {
            // Permission already granted
            permissionGrantedCallback?.invoke()
        }
    }

    // Explain to the user why the app needs permission and re-prompt
    private fun showPermissionExplanationDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Permission Needed")
        builder.setMessage("This app requires access to your external storage to save and read files. Please grant the permission.")

        builder.setPositiveButton("Grant") { _, _ ->
            // Re-prompt for permission
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_CODE
            )
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    // Call this method from onRequestPermissionsResult to handle permission denial
    fun handlePermissionResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                permissionGrantedCallback?.invoke()
            } else {
                // Permission denied
                if (!ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // If permission was denied permanently, show a dialog directing to settings
                    showGoToSettingsDialog()
                } else {
                    // Permission was denied but not permanently
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Dialog to prompt the user to go to settings if permission is permanently denied
    private fun showGoToSettingsDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Permission Permanently Denied")
        builder.setMessage("You have permanently denied the storage permission. You can enable it in the app settings.")

        builder.setPositiveButton("Go to Settings") { _, _ ->
            // Open app settings
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }
}
