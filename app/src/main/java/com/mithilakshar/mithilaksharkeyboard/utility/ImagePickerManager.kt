package com.mithilakshar.mithilaksharkeyboard.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import android.Manifest
import android.os.Build

import android.provider.Settings

class ImagePicker(
    private val context: Context,
    private val activityResultLauncher: ActivityResultLauncher<Intent>,
    private val onImagePicked: (Uri) -> Unit
) {

    fun checkAndRequestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            launchImagePicker()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                // Show rationale if needed
                showPermissionRationale {
                    ActivityCompat.requestPermissions(context, arrayOf(permission), REQUEST_CODE)
                }
            } else {
                // Directly request the permission
                ActivityCompat.requestPermissions(context as Activity, arrayOf(permission), REQUEST_CODE)
            }
        }
    }

    private fun showPermissionRationale(onPositive: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Permission Required")
            .setMessage("This app requires access to your storage to pick an image. Please grant the permission.")
            .setPositiveButton("Allow") { dialog, _ ->
                dialog.dismiss()
                onPositive()
            }
            .setNegativeButton("Deny") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(context, "Permission denied. Unable to pick image.", Toast.LENGTH_SHORT).show()
                // Optionally guide user to settings if "Don't ask again" was selected
                if (!ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showSettingsDialog()
                }
            }
            .create()
            .show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(context)
            .setTitle("Permission Required")
            .setMessage("The app needs permission to access your storage. Please enable it in the app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun launchImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intent)
    }

    fun handleActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            selectedImageUri?.let {
                onImagePicked(it)
            }
        }
    }

    fun handlePermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, launch image picker
                launchImagePicker()
            } else {
                // Check if we need to show rationale
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show rationale and request permission again
                    showPermissionRationale {
                        ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
                    }
                } else {
                    // Permission denied and "Don't ask again" checked; handle it gracefully
                    Toast.makeText(context, "Permission denied. Unable to pick image.", Toast.LENGTH_SHORT).show()
                    showSettingsDialog()
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 1001
    }
}
