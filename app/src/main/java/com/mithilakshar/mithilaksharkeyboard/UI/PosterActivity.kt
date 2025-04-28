package com.mithilakshar.mithilaksharkeyboard.UI

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.mithilakshar.mithilaksharkeyboard.Dialog.Networkdialog
import com.mithilakshar.mithilaksharkeyboard.R
import com.mithilakshar.mithilaksharkeyboard.Room.UpdatesDao
import com.mithilakshar.mithilaksharkeyboard.Room.UpdatesDatabase
import com.mithilakshar.mithilaksharkeyboard.databinding.ActivityPosterBinding
import com.mithilakshar.mithilaksharkeyboard.utility.FirebaseFileDownloader
import com.mithilakshar.mithilaksharkeyboard.utility.NetworkManager
import com.mithilakshar.mithilaksharkeyboard.utility.UpdateChecker
import com.mithilakshar.mithilaksharkeyboard.utility.dbHelper
import com.mithilakshar.mithilaksharkeyboard.utility.sViewDownloader
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PosterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPosterBinding
    private lateinit var updatesDao: UpdatesDao
    private lateinit var updateChecker: UpdateChecker
    private lateinit var dbHelper: dbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPosterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up edge-to-edge UI
        setupEdgeToEdgeLayout()

        // Set up network monitoring
        setupNetworkMonitoring()

        // Initialize Room DAO, UpdateChecker and DB Helper
        updatesDao = UpdatesDatabase.getDatabase(applicationContext).UpdatesDao()
        updateChecker = UpdateChecker(updatesDao, this)
        dbHelper = dbHelper(this, "posterlist.db")

        // Check for updates in a coroutine
        checkForUpdates()
        binding.lottieAnimationView.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE

        // Set up FAB click listener to navigate to LayoutGenerator activity
        binding.fab.setOnClickListener {
            navigateToLayoutGenerator()
        }
    }

    private fun setupEdgeToEdgeLayout() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupNetworkMonitoring() {
        val networkDialog = Networkdialog(this)
        val networkManager = NetworkManager(this)

        networkManager.observe(this, { isConnected ->
            if (isConnected) {
                networkDialog.dismiss()
            } else if (!networkDialog.isShowing) {
                networkDialog.show()
            }
        })
    }

    private fun checkForUpdates() {
        MainScope().launch {
            try {
                val updateStatus = updateChecker.getUpdateStatus()
                val action = updateStatus["action"]
                val url = updateStatus["url"]

                Log.d("PosterActivity", "Action: $action, URL: $url")

                if (action != "a" && !url.isNullOrEmpty()) {
                    Log.d("PosterActivity", "Update required. Starting download...")
                    downloadFile(url)
                } else {
                    Log.d("PosterActivity", "No update needed.")
                    // Update the UI with the data from the database
                    updateUIWithPosterItems()
                }
            } catch (e: Exception) {
                Log.e("PosterActivity", "Error checking for updates", e)
            }
        }
    }

    private fun downloadFile(url: String) {
        val fileName = "posterlist.db"
        Log.d("PosterActivity", "Starting download for URL: $url, File: $fileName")

        val firebaseFileDownloader = FirebaseFileDownloader(this)
        firebaseFileDownloader.download(url, fileName) { downloadedFile ->
            if (downloadedFile != null) {
                Log.d("PosterActivity", "Download completed successfully: ${downloadedFile.absolutePath}")
                showDownloadCompleteUI(downloadedFile)
            } else {
                Log.e("PosterActivity", "Download failed.")
                showDownloadFailedUI()
            }
        }
    }

    private fun showDownloadCompleteUI(downloadedFile: File) {
        Log.d("PosterActivity", "File downloaded: ${downloadedFile.absolutePath}")
        Toast.makeText(this, "Download Complete: ${downloadedFile.name}", Toast.LENGTH_SHORT).show()

        // After successful download, update the UI with new poster items
        updateUIWithPosterItems()
    }

    private fun showDownloadFailedUI() {
        Toast.makeText(this, "Download Failed. Please try again.", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLayoutGenerator() {
        Log.d("PosterActivity", "FAB clicked, navigating to LayoutGenerator activity.")
        val intent = Intent(this, LayoutGenerator::class.java)
        startActivity(intent)
    }

    // Update the UI with data from the database
    private fun updateUIWithPosterItems() {
        // Fetch all poster items from the database
        val posterItems = dbHelper.getAllPosterItems()
        Log.d("PosterActivity", "Poster items fetched: $posterItems")

        if (posterItems.isNotEmpty()) {
            // Display the RecyclerView and set the adapter with onItemClick lambda
            binding.recyclerView.apply {
                adapter = PosterAdapter(posterItems) { imageUrl ->
                    // Handle item click, show dialog with imageUrl
                    showPosterItemDialog(imageUrl)
                }
                visibility = View.VISIBLE
            }

            // Hide the Lottie animation when items are available
            binding.lottieAnimationView.visibility = View.GONE
        } else {
            // No items found, display a message and show Lottie animation
            Log.w("PosterActivity", "No poster items found in the database.")
            Toast.makeText(this, "No poster items found.", Toast.LENGTH_SHORT).show()

            // Optionally, keep Lottie animation visible if no items are found
            binding.lottieAnimationView.visibility = View.VISIBLE
        }
    }

    private fun showPosterItemDialog(posterItem: Map<String, String>) {
        // Extract the image URL from the map
        val imageUrl = posterItem["url"] ?: ""  // Safely get the URL or use an empty string if it's null

        // Log the image URL to make sure it is being passed correctly
        Log.d("PosterDialog", "Image URL: $imageUrl")

        // Inflate the custom dialog layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_poster_item, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)

        val dialog = dialogBuilder.create()
        dialog.show()

        // Find views
        val dialogImageView: ImageView = dialogView.findViewById(R.id.dialogImageView)
        val downloadButton: ImageView = dialogView.findViewById(R.id.downloadImage)
        val shareButton : ImageView = dialogView.findViewById(R.id.shareImage)
        val shareview: LinearLayout = dialogView.findViewById(R.id.shareview)

        // Log to make sure the ImageView is being found
        Log.d("PosterDialog", "dialogImageView: $dialogImageView")

        // Set image using Glide
        Glide.with(this)
            .load(imageUrl)
            .into(dialogImageView)

        // Share button click
        shareButton.setOnClickListener {
            shareImage(shareview)
            dialog.dismiss() // Dismiss the dialog
        }

        // Download button click
        downloadButton.setOnClickListener {
            downloadImage(shareview)
            dialog.dismiss() // Dismiss the dialog
        }

        // Set dialog width to match parent (screen width)
        val layoutParams = dialog.window?.attributes
        layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window?.attributes = layoutParams
    }




    private fun shareImage(view: View) {
        // Initialize the sViewDownloader class
        val sViewDownloader = sViewDownloader(this)

        // Use the sViewDownloader to convert the view to a bitmap
        sViewDownloader.shareBitmapAsImage(view, this)
    }




    private fun downloadImage(view: View) {
        // Generate a random file name using UUID
        val randomFileName = "mithilakshar#" + UUID.randomUUID().toString()

        // Initialize the sViewDownloader class
        val sViewDownloader = sViewDownloader(this)

        // Show a Toast indicating that the download is starting
        Toast.makeText(this, "Downloading image...", Toast.LENGTH_SHORT).show()

        // Use the sViewDownloader to download the image as a bitmap and save it with the random file name
        sViewDownloader.downloadViewAsImage(view, randomFileName, this)
    }





}
