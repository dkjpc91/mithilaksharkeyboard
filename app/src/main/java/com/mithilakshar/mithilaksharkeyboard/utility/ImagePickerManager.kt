package com.mithilakshar.mithilaksharkeyboard.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import android.Manifest
import android.os.Build

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

        when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted, proceed with picking the image
                launchImagePicker()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission) -> {
                // Show a custom dialog explaining why the permission is needed
                showPermissionRationale {
                    ActivityCompat.requestPermissions(context, arrayOf(permission), REQUEST_CODE)
                }
            }
            else -> {
                // Directly request the permission
                ActivityCompat.requestPermissions(context, arrayOf(permission), REQUEST_CODE)
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
            }
            .create()
            .show()
    }

    fun launchImagePicker() {
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
                launchImagePicker()
            } else {
                // Handle the case where permission is denied
                Toast.makeText(context, "Permission denied. Unable to pick image.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 1001
    }
}
