package com.mithilakshar.mithilaksharkeyboard.utility

import PermissionManager
import android.app.Activity
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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mithilakshar.mithilaksharkeyboard.R
import com.mithilakshar.mithilaksharkeyboard.Room.UpdatesDao
import kotlinx.coroutines.launch


class CustomMenu(
    private val context: Context,
    private val main: View,
    private val imagePicker: ImagePicker,
    private val imagelyoutadder: Imagelyoutadder,
    private val updatesDao: UpdatesDao,
    private val  rewardAdManager: RewardAdManager

) {
    val viewDownloader = sViewDownloader(context)

    var lifecycleOwner = main.findViewTreeLifecycleOwner()
    private val uniqueStringLiveData = MutableLiveData<String>()



    fun showDialog() {
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(R.layout.custommenu, null)

        val builder = AlertDialog.Builder(context)
            .setView(customView)
            .setCancelable(true)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        rewardAdManager.loadRewardedAd(
            onAdLoaded = {
                Toast.makeText(context, "Rewarded Ad Loaded", Toast.LENGTH_SHORT).show()
            },
            onAdFailedToLoad = { error ->
                Toast.makeText(context, "Failed to load ad: $error", Toast.LENGTH_SHORT).show()
            }
        )
        val share: LinearLayout = customView.findViewById(R.id.share)
        val download: LinearLayout = customView.findViewById(R.id.download)
        val add: LinearLayout = customView.findViewById(R.id.add)
        val posterbackgroundlist: LinearLayout = customView.findViewById(R.id.posterbackgroundlist)
        val posterbackgroundprelist : LinearLayout = customView.findViewById(R.id.posterbackgroundprelist)
        val mithilakshar : LinearLayout = customView.findViewById(R.id.mithilakshar)
        val devnagri : LinearLayout = customView.findViewById(R.id.devnagri)
        val imagelist : LinearLayout = customView.findViewById(R.id.imagelist)
        val imageselection : LinearLayout = customView.findViewById(R.id.imageselection)
        val shareapp : LinearLayout = customView.findViewById(R.id.shareapp)


         lifecycleOwner = main.findViewTreeLifecycleOwner()
        if (lifecycleOwner != null) {
            updatesDao.getUniqueStringById(2).observe(lifecycleOwner!!, {
                uniqueStringLiveData.postValue(it)
                if(it.equals("d")){

                    share.visibility=View.GONE
                    download.visibility=View.GONE
                    add.visibility=View.VISIBLE
                }

            })
        }

        add.setOnClickListener {




            showRewardAd(share,download,add)



        }

        shareapp.setOnClickListener {
            // Ensure 'context' is the proper context for the dialog, e.g., activity or application context
            val shareText = "नीचा देल गेल लिंक पर क्लिक क मिथिला पंचांग ऐप्प डाउनलोड करू .\n https://play.google.com/store/apps/details?id=${context.packageName} \n\n\n @mithilakshar13"

            // Create the intent to share the text
            val intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            // Start the sharing activity
            context.startActivity(Intent.createChooser(intent, "साझा करू : "))
        }


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
            val dbHelper=dbHelper(context,"Imageslist.db")
            val imageResources = getUrls(dbHelper.getImageList())
            val imageLayoutAdderUrl = ImageLayoutAdderUrl(context, (main as RelativeLayout))
            val imageSelectorDialog = ImageSelectorDialog(
                context = context,
                imageMap = imageResources,
                onImageSelected = { selectedImage ->

                    imageLayoutAdderUrl.addImageViewFromUrl(selectedImage.toString())


                    alertDialog.dismiss()
                }
            )

            imageSelectorDialog.show()



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
            val imageResources = listOf(
                R.drawable.page,
                R.drawable.page1,
                R.drawable.up
            )
            val imageSelectorDialog = ImageSelectorDialog(
                context = context,
                imageResources,
                onImageSelected = { selectedImage ->
                    main.setBackgroundResource(selectedImage as Int)
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

            lifecycleOwner?.lifecycleScope?.launch {

                updateUniqueString(lifecycleOwner!!,updatesDao,uniqueStringLiveData.value.toString())

            }


            viewDownloader.downloadViewAsImage(main, "mithila${System.currentTimeMillis()}", context)
            Toast.makeText(context, "पोस्टर फ़ोन में सेव भ गेल", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
            showSecondDialog()
        }

        share.setOnClickListener {
            lifecycleOwner?.lifecycleScope?.launch {

                updateUniqueString(lifecycleOwner!!,updatesDao,uniqueStringLiveData.value.toString())

            }

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


    fun getUrls(mapList: List<Map<String, Any?>>): List<String> {
        return mapList.mapNotNull { map ->
            // Extract the value associated with the "url" key and ensure it's a String
            (map["url"] as? String)
        }
    }
    private fun showRewardAd(share: LinearLayout, download: LinearLayout, add: LinearLayout) {
        rewardAdManager.showRewardedAd((context as Activity),
            onUserEarnedReward = {
                lifecycleOwner?.let { updateUniqueString(it,updatesDao,"a") }

                Toast.makeText(context, "User earned reward!", Toast.LENGTH_SHORT).show()

                share.visibility=View.VISIBLE
                download.visibility=View.VISIBLE
                add.visibility=View.GONE
                // Handle the reward logic here
            },
            onAdNotLoaded = {
                lifecycleOwner?.let { updateUniqueString(it,updatesDao,"b") }
                share.visibility=View.VISIBLE
                download.visibility=View.VISIBLE
                add.visibility=View.GONE
                Toast.makeText(context, "Ad is not loaded yet", Toast.LENGTH_SHORT).show()
            }
        )
    }


    fun updateUniqueString(
        lifecycleOwner: LifecycleOwner,
        updatesDao: UpdatesDao,
        uniqueStringLiveData: String
    ) {
        lifecycleOwner.lifecycleScope.launch {
            val addupdate = updatesDao.findById(2)
            addupdate?.let {
                val newUniqueString = when (uniqueStringLiveData) {
                    "a" -> "b"
                    "b" -> "c"
                    "c" -> "d"
                    else -> null
                }
                newUniqueString?.let {
                    updatesDao.updateUniqueStringById(2, newUniqueString)
                }
            }
        }
    }

}




