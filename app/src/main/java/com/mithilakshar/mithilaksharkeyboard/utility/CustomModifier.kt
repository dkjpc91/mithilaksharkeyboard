import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.mithilakshar.mithilaksharkeyboard.R
import com.mithilakshar.mithilaksharkeyboard.adapter.ImageAdapter
import com.mithilakshar.mithilaksharkeyboard.adapter.ImagedburlAdapter
import com.mithilakshar.mithilaksharkeyboard.utility.dbHelper
import com.mithilakshar.mithilaksharkeyboard.utility.sViewDownloader

class CustomModifier(private val context: Context) {

    private val viewDownloader = sViewDownloader(context)

    // Main method to show the dialog with customizable options
    fun showDialog(
        textView1: TextView,    // Custom TextView1
        textView2: TextView,    // Custom TextView2
        imageView1: ImageView,  // Custom ImageView1
        imageView2: ImageView,   // Custom ImageView2
        bgView: LinearLayout,
        txt1: String,
        txt2: String,
        image1Url: String?,
        image2Url: String?,
        bgUrl: String?
    ) {
        val customView = LayoutInflater.from(context).inflate(R.layout.custommodifier, null)
        val alertDialog = createAlertDialog(customView)


        // Set text
        textView1.text = txt1
        textView2.text = txt2

        // Find views in customView
        val bgImageView1 = customView.findViewById<ImageView>(R.id.bgimage1)
        val bgImageView2 = customView.findViewById<ImageView>(R.id.bgimage2)
        val rightImageView1 = customView.findViewById<ImageView>(R.id.rightImageView1)
        val rightImageView2 = customView.findViewById<ImageView>(R.id.rightImageView2)
        val rightTextView1 = customView.findViewById<TextView>(R.id.rightTextView1)
        val rightTextView2 = customView.findViewById<TextView>(R.id.rightTextView2)

        rightTextView1.text = txt1
        rightTextView2.text = txt2
        // Load right images if URLs are provided
        if (!image1Url.isNullOrEmpty()) {
            Glide.with(context)
                .load(image1Url)
                .placeholder(R.drawable.m)  // Optional placeholder image
                .error(R.drawable.mithilakshar) // Optional error image
                .into(rightImageView1)
        }

        if (!image2Url.isNullOrEmpty()) {
            Glide.with(context)
                .load(image2Url)
                .placeholder(R.drawable.m)
                .error(R.drawable.mithilakshar)
                .into(rightImageView2)
        }

// Load background images if URLs are provided
        if (!bgUrl.isNullOrEmpty()) {
            Glide.with(context)
                .load(bgUrl)
                .into(bgImageView1)
        }

        if (!bgUrl.isNullOrEmpty()) {
            Glide.with(context)
                .load(bgUrl)
                .into(bgImageView2)
        }

        initializeViews(customView, textView1, textView2, imageView1, imageView2, alertDialog,bgView,bgImageView1,bgImageView2,rightImageView1,rightImageView2,rightTextView1,rightTextView2)
        alertDialog.show()
    }

    // Helper method to create an AlertDialog
    private fun createAlertDialog(customView: android.view.View): AlertDialog {
        return AlertDialog.Builder(context)
            .setView(customView)
            .setCancelable(true)
            .create().apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
            }
    }

    // Initialize all views in the dialog and set click listeners
    private fun initializeViews(
        customView: View,
        textView1: TextView,
        textView2: TextView,
        imageView1: ImageView,
        imageView2: ImageView,
        alertDialog: AlertDialog,
        bgView: LinearLayout,
        bgImageView1: ImageView,
        bgImageView2: ImageView,
        rightImageView1: ImageView,
        rightImageView2: ImageView,
        rightTextView1: TextView,
        rightTextView2: TextView
    ) {
        customView.apply {
            findViewById<LinearLayout>(R.id.share).setOnClickListener {
                sharePoster()
                alertDialog.dismiss()
            }

            findViewById<LinearLayout>(R.id.download).setOnClickListener {
                downloadPoster()
                alertDialog.dismiss()
                showSecondDialog()
            }

            findViewById<LinearLayout>(R.id.add).setOnClickListener {
                // Additional logic for add, if any
            }

            findViewById<LinearLayout>(R.id.shareapp).setOnClickListener {
                shareAppLink()
                alertDialog.dismiss()
            }

            findViewById<LinearLayout>(R.id.mithilakshar).setOnClickListener {
                showCustomEditDialog(textView1, rightTextView1 )
                alertDialog.dismiss()
            }

            findViewById<LinearLayout>(R.id.devnagri).setOnClickListener {
                showCustomEditDialog(textView2,rightTextView2)
                alertDialog.dismiss()
            }

            findViewById<LinearLayout>(R.id.imagelist).setOnClickListener {
                // Show a dialog with a list of images to choose from
                showImagebgurlSelectorDialog3(imageView1, alertDialog,rightImageView1)
                alertDialog.dismiss()
            }
            findViewById<LinearLayout>(R.id.imageselection).setOnClickListener {
                // Show a dialog with a list of images to choose from
                showImagebgurlSelectorDialog2(imageView2, alertDialog,rightImageView2)
                alertDialog.dismiss()
            }

            findViewById<LinearLayout>(R.id.posterbackgroundprelist).setOnClickListener {
                // Show a dialog with a list of images to choose from
                showImagebgSelectorDialog(bgView, alertDialog,bgImageView1)
                alertDialog.dismiss()
            }

            findViewById<LinearLayout>(R.id.posterbackgroundlist).setOnClickListener {
                // Show a dialog with a list of images to choose from
                showImagebgurlSelectorDialog(bgView, alertDialog,bgImageView2)
                alertDialog.dismiss()
            }


        }
    }


    private fun showImagebgSelectorDialog(
        customView: View,
        alertDialog: AlertDialog,
        bgImageView1: ImageView
    ) {
        val imageResourceIds = listOf(
            R.drawable.page, R.drawable.page1, R.drawable.page2,
            R.drawable.page3, R.drawable.page4, R.drawable.page5,
            R.drawable.page6, R.drawable.page7, R.drawable.page8,
            R.drawable.page9
        )


        val inflater = LayoutInflater.from(customView.context)
        val imageSelectorView = inflater.inflate(R.layout.image_selector_dialog, null)

        val recyclerView: RecyclerView = imageSelectorView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(customView.context, 2)

        // Create the dialog first
        val imageSelectorDialog = AlertDialog.Builder(customView.context)
            .setView(imageSelectorView)
            .setCancelable(true)
            .create()

        // Create and set the ImageAdapter to the RecyclerView
        val imageAdapter = ImageAdapter(imageResourceIds) { selectedImage ->
            try {
                customView.setBackgroundResource(selectedImage)
                bgImageView1.setImageResource(selectedImage)
                customView.invalidate()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            imageSelectorDialog.dismiss() // Dismiss the image selection dialog
            alertDialog.dismiss() // Dismiss the original alert dialog
        }

        recyclerView.adapter = imageAdapter

        imageSelectorDialog.show()
    }




    private fun showImagebgurlSelectorDialog(
        customView: View,
        alertDialog: AlertDialog,
        bgImageView2: ImageView
    ) {
        // Fetch image list from database
        val dbHelper = dbHelper(context, "layouthelper.db")
        val imageResources = dbHelper.getImageBgList()

        // Inflate the custom dialog layout
        val inflater = LayoutInflater.from(context)
        val imageSelectorView = inflater.inflate(R.layout.image_selector_dialog, null)

        val recyclerView: RecyclerView = imageSelectorView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)  // 2 columns

        // Create the image selector dialog
        val imageSelectorDialog = AlertDialog.Builder(context)
            .setView(imageSelectorView)
            .setCancelable(true)
            .create()

        // Create and set the ImagedburlAdapter
        val imageAdapter = ImagedburlAdapter(imageResources) { selectedImage ->
            try {
                // Apply the selected image as background to customView
                Glide.with(context)
                    .load(selectedImage)
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            customView.background = resource  // Set as background
                        }
                    })

                // Load the same image into bgImageView2
                Glide.with(context)
                    .load(selectedImage)
                    .into(bgImageView2)  // Set as image in ImageView

                // Refresh views
                customView.invalidate()
                bgImageView2.invalidate()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            imageSelectorDialog.dismiss() // Close the image selector dialog
            alertDialog.dismiss() // Close the original alert dialog
        }

        recyclerView.adapter = imageAdapter

        imageSelectorDialog.show()
    }


    private fun showImagebgurlSelectorDialog2(
        customView: View,
        alertDialog: AlertDialog,
        bgImageView2: ImageView
    ) {
        // Fetch image list from database
        val dbHelper = dbHelper(context, "layouthelper.db")
        val imageResources = dbHelper.getImage1List()

        // Inflate the custom dialog layout
        val inflater = LayoutInflater.from(context)
        val imageSelectorView = inflater.inflate(R.layout.image_selector_dialog, null)

        val recyclerView: RecyclerView = imageSelectorView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)  // 2 columns

        // Create the image selector dialog
        val imageSelectorDialog = AlertDialog.Builder(context)
            .setView(imageSelectorView)
            .setCancelable(true)
            .create()

        // Create and set the ImagedburlAdapter
        val imageAdapter = ImagedburlAdapter(imageResources) { selectedImage ->
            try {
                // Apply the selected image as background to customView
                Glide.with(context)
                    .load(selectedImage)
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            customView.background = resource  // Set as background
                        }
                    })

                // Load the same image into bgImageView2
                Glide.with(context)
                    .load(selectedImage)
                    .into(bgImageView2)  // Set as image in ImageView

                // Refresh views
                customView.invalidate()
                bgImageView2.invalidate()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            imageSelectorDialog.dismiss() // Close the image selector dialog
            alertDialog.dismiss() // Close the original alert dialog
        }

        recyclerView.adapter = imageAdapter

        imageSelectorDialog.show()
    }

    private fun showImagebgurlSelectorDialog3(
        customView: View,
        alertDialog: AlertDialog,
        bgImageView2: ImageView
    ) {
        // Fetch image list from database
        val dbHelper = dbHelper(context, "layouthelper.db")
        val imageResources = dbHelper.getImage2List()

        // Inflate the custom dialog layout
        val inflater = LayoutInflater.from(context)
        val imageSelectorView = inflater.inflate(R.layout.image_selector_dialog, null)

        val recyclerView: RecyclerView = imageSelectorView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)  // 2 columns

        // Create the image selector dialog
        val imageSelectorDialog = AlertDialog.Builder(context)
            .setView(imageSelectorView)
            .setCancelable(true)
            .create()

        // Create and set the ImagedburlAdapter
        val imageAdapter = ImagedburlAdapter(imageResources) { selectedImage ->
            try {
                // Apply the selected image as background to customView
                Glide.with(context)
                    .load(selectedImage)
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            customView.background = resource  // Set as background
                        }
                    })

                // Load the same image into bgImageView2
                Glide.with(context)
                    .load(selectedImage)
                    .into(bgImageView2)  // Set as image in ImageView

                // Refresh views
                customView.invalidate()
                bgImageView2.invalidate()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            imageSelectorDialog.dismiss() // Close the image selector dialog
            alertDialog.dismiss() // Close the original alert dialog
        }

        recyclerView.adapter = imageAdapter

        imageSelectorDialog.show()
    }






    private fun showImageSelectorDialog(
        imageView1: ImageView,
        alertDialog: AlertDialog,
        rightImageView1: ImageView
    ) {


        val imageResourceIds = listOf(
            R.drawable.page, R.drawable.page1, R.drawable.page2,
            R.drawable.page3, R.drawable.page4, R.drawable.page5,
            R.drawable.page6, R.drawable.page7, R.drawable.page8,
            R.drawable.page9
        )

        // Inflate the custom dialog layout
        val inflater = LayoutInflater.from(context)
        val imageSelectorView = inflater.inflate(R.layout.image_selector_dialog, null)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = imageSelectorView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2) // Grid layout with 2 columns

        // Create the image selector dialog
        val imageSelectorDialog = AlertDialog.Builder(context)
            .setView(imageSelectorView)
            .setCancelable(true)
            .create()

        // Create and set the ImageAdapter to the RecyclerView
        val imageAdapter = ImageAdapter(imageResourceIds) { selectedImage ->
            // Handle the item click: update the images
            imageView1.setImageResource(selectedImage)
            rightImageView1.setImageResource(selectedImage)

            // Dismiss both dialogs
            imageSelectorDialog.dismiss()
            alertDialog.dismiss()
        }

        recyclerView.adapter = imageAdapter

        // Show the image selector dialog
        imageSelectorDialog.show()
    }




    // Method to show a custom dialog to edit the text content
    private fun showCustomEditDialog(targetTextView: TextView, rightTextView1: TextView, ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.edit_text_dialog, null)
        val editText = dialogView.findViewById<EditText>(R.id.editText)
        val seekBar = dialogView.findViewById<SeekBar>(R.id.seekBarTextSize)
        val colorPickerButton = dialogView.findViewById<Button>(R.id.colorPickerButton)
        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)

        // Initialize with the current text from the TextView
        editText.setText(targetTextView.text)
        seekBar.progress = 16  // default text size

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Handle color picker button click
        colorPickerButton.setOnClickListener {
            ColorPickerDialog(context, object : ColorPickerDialog.ColorPickerListener {
                override fun onColorSelected(color: Int) {
                    targetTextView.setTextColor(color)
                }
            }).show()
        }

        // Handle SeekBar for text size adjustment
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                targetTextView.textSize = (progress + 10).toFloat()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Handle save button to update the TextView's content
        saveButton.setOnClickListener {
            targetTextView.text = editText.text.toString()
            rightTextView1.text=editText.text.toString()
            dialog.dismiss()
        }

        dialog.show()
    }

    // Share app link
    private fun shareAppLink() {
        val shareText = "नीचा देल गेल लिंक पर क्लिक क मिथिलाक्षर पोस्टर ऐप्प डाउनलोड करू .\n https://play.google.com/store/apps/details?id=${context.packageName} \n\n\n @mithilakshar13"
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(intent, "साझा करू : "))
    }

    // Download the poster and show a confirmation message
    private fun downloadPoster() {
        viewDownloader.downloadViewAsImage(
            (context as Activity).findViewById<FrameLayout>(R.id.layoutContainer),
            "mithila${System.currentTimeMillis()}",
            context
        )
        Toast.makeText(context, "पोस्टर फ़ोन में सेव भ गेल", Toast.LENGTH_SHORT).show()
    }

    // Share the poster image
    private fun sharePoster() {
        viewDownloader.shareBitmapAsImage(
            (context as Activity).findViewById<FrameLayout>(R.id.layoutContainer),
            context
        )
        Toast.makeText(context, "पोस्टर के शेयर करू मनपसंद ऐप पर ", Toast.LENGTH_SHORT).show()
    }

    // Show the second dialog for confirmation or other actions
    private fun showSecondDialog() {
        val secondDialogView = LayoutInflater.from(context).inflate(R.layout.prompt, null)
        val secondDialog = AlertDialog.Builder(context)
            .setView(secondDialogView)
            .setCancelable(true)
            .create()

        secondDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        secondDialog.show()
    }


}
