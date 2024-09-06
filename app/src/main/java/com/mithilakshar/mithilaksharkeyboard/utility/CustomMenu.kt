package com.mithilakshar.mithilaksharkeyboard.utility

import PermissionManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import com.mithilakshar.mithilaksharkeyboard.R


class CustomMenu(
    private val context: Context,
    private val main: View,
    private val  imagePicker: ImagePicker,
    private val  imagelyoutadder: Imagelyoutadder,
    private val activityResultLauncher: ActivityResultLauncher<String>

) {
    val viewDownloader = sViewDownloader(context)

/*    private val permissionManager: PermissionManager = PermissionManager(
        context = context,
        activityResultLauncher = activityResultLauncher,
        onPermissionGranted = {
            // Define what happens when permission is granted
            // For instance, you might call imagePicker or imagelyoutadder
            Toast.makeText(context, "permission granted.", Toast.LENGTH_SHORT).show()
        }
    )*/

    fun showDialog() {
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(R.layout.custommenu, null)

        val builder = AlertDialog.Builder(context)
            .setView(customView)
            .setCancelable(true)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        val share: LinearLayout = customView.findViewById(R.id.share)
        val download: LinearLayout = customView.findViewById(R.id.download)
        val posterbackgroundlist: LinearLayout = customView.findViewById(R.id.posterbackgroundlist)
        val posterbackgroundprelist : LinearLayout = customView.findViewById(R.id.posterbackgroundprelist)
        val mithilakshar : LinearLayout = customView.findViewById(R.id.mithilakshar)
        val devnagri : LinearLayout = customView.findViewById(R.id.devnagri)
        val imagelist : LinearLayout = customView.findViewById(R.id.imagelist)
        val imageselection : LinearLayout = customView.findViewById(R.id.imageselection)

        imagelist.setOnClickListener {
            showPermissionDialog(
                toastMessage = "Audio Permission Granted",
                onPermissionGranted = {
                    // Task specific to audio selection
                    // Add your audio selection task here
                    Toast.makeText(context, "succes imageselector", Toast.LENGTH_SHORT).show()
                    imagelyoutadder.showImagePickerDialog(main as RelativeLayout)
                }
            )


        }

        imageselection.setOnClickListener {


            alertDialog.dismiss()
            showPermissionDialog(
                toastMessage = "Audio Permission Granted",
                onPermissionGranted = {
                    // Task specific to audio selection
                    // Add your audio selection task here
                    Toast.makeText(context, "succes permission", Toast.LENGTH_SHORT).show()
                }
            )


        }

        mithilakshar.setOnClickListener {

            val textViewAdder = TextViewAdder(context, (main as RelativeLayout),true)
            textViewAdder.showTextInputDialog()

            alertDialog.dismiss()


        }
        devnagri.setOnClickListener {
            val textViewAdder = TextViewAdder(context, (main as RelativeLayout),false)
            textViewAdder.showTextInputDialog()

            alertDialog.dismiss()

        }

        posterbackgroundprelist.setOnClickListener {
            Log.d("CustomMenu", "reached here")

            val imageSelectorDialog = ImageSelectorDialog(
                context = context,
                onImageSelected = { selectedImage ->
                    main.setBackgroundResource(selectedImage)
                    alertDialog.dismiss()
                }
            )

            imageSelectorDialog.show()


        }

        posterbackgroundlist.setOnClickListener {
            Log.d("CustomMenu", "reached here")

            showPermissionDialog(
                toastMessage = "Audio Permission Granted",
                onPermissionGranted = {
                    // Task specific to audio selection
                    // Add your audio selection task here
                    Toast.makeText(context, "succes imageselector", Toast.LENGTH_SHORT).show()
                    imagePicker.checkAndRequestPermission()
                }
            )


            alertDialog.dismiss()
        }

        download.setOnClickListener {
            viewDownloader.downloadViewAsImage(main, "mithila${System.currentTimeMillis()}", context)
            Toast.makeText(context, "पोस्टर फ़ोन में सेव भ गेल", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
            showSecondDialog()
        }

        share.setOnClickListener {
            viewDownloader.shareBitmapAsImage(main, context)
            Toast.makeText(context, "पोस्टर के शेयर  करू मनपसंद ऐप पर ", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun showSecondDialog() {
        val inflater = LayoutInflater.from(context)
        val secondDialogView = inflater.inflate(R.layout.prompt, null)

        val builder = AlertDialog.Builder(context)
            .setTitle("Second Dialog")
            .setView(secondDialogView)
            .setCancelable(true)

        val buttonOk: Button = secondDialogView.findViewById(R.id.button_ok)
        val buttonCancel: Button = secondDialogView.findViewById(R.id.button_cancel)

        buttonOk.setOnClickListener {
            Toast.makeText(context, "OK clicked", Toast.LENGTH_SHORT).show()
            builder.create().dismiss()
        }

        buttonCancel.setOnClickListener {
            Toast.makeText(context, "Cancel clicked", Toast.LENGTH_SHORT).show()
            builder.create().dismiss()
        }

        val secondDialog: AlertDialog = builder.create()
        secondDialog.show()
    }

    private fun setImageAsBackground(view: View, imageUri: Uri) {
        val imageStream = context.contentResolver.openInputStream(imageUri)
        val drawable = Drawable.createFromStream(imageStream, imageUri.toString())
        view.background = drawable
    }






    private fun showPermissionDialog(
        toastMessage: String,
        onPermissionGranted: () -> Unit
    ) {

        PermissionManager(context).checkStoragePermission {
            // This code is executed when the permission is granted
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            onPermissionGranted()
        }

    }


}




