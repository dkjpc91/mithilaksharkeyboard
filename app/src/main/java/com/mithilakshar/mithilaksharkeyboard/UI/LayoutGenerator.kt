package com.mithilakshar.mithilaksharkeyboard.UI

import RawFileCopier
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.mithilakshar.mithilaksharkeyboard.Dialog.Networkdialog
import com.mithilakshar.mithilaksharkeyboard.MainActivity
import com.mithilakshar.mithilaksharkeyboard.R
import com.mithilakshar.mithilaksharkeyboard.adapter.LayoutAdapter
import com.mithilakshar.mithilaksharkeyboard.databinding.ActivityLayoutGeneratorBinding
import com.mithilakshar.mithilaksharkeyboard.utility.NetworkManager
import com.mithilakshar.mithilaksharkeyboard.utility.dbHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import kotlin.time.Duration.Companion.seconds

class LayoutGenerator : AppCompatActivity() {

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.IMMEDIATE

    private lateinit var binding: ActivityLayoutGeneratorBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LayoutAdapter
    private lateinit var databaseHelper: dbHelper
    private lateinit var lottieAnimationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLayoutGeneratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        if (updateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.registerListener(installStateUpdatedListener)
        }
        checkForAppUpdate()
        val networkdialog = Networkdialog(this)
        val networkManager = NetworkManager(this)
        networkManager.observe(this, {
            if (!it) {
                if (!networkdialog.isShowing) {
                    networkdialog.show()
                }

            } else {
                if (networkdialog.isShowing) {
                    networkdialog.dismiss()
                }

            }
        })



        Log.d("LayoutGenerator", "Starting file copy process")
        recyclerView = findViewById(R.id.recyclerView)
        lottieAnimationView = binding.lottieAnimationView

        playLottieAnimations()

        // Load RecyclerView after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            binding.lottieAnimationView.visibility=View.GONE
            recyclerView.visibility = View.VISIBLE
            copyFilesAndLoadData()
        }, 4000) // 3000 milliseconds = 3 seconds

        binding.fab.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        binding.tester.setOnClickListener {
            val intent = Intent(this, LayoutTester::class.java)
            startActivity(intent)

        }




    }

    private fun playLottieAnimations() {
        recyclerView.visibility = View.GONE
        lottieAnimationView.visibility = View.VISIBLE

        // Play first animation (a3.json)
        lottieAnimationView.setAnimation(R.raw.a3)
        lottieAnimationView.playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            // Play second animation (a4.json)
            lottieAnimationView.setAnimation(R.raw.om)
            lottieAnimationView.playAnimation()
        }, 1500) // Switch animation after 1.5 seconds

        Handler(Looper.getMainLooper()).postDelayed({
            // Hide Lottie and show RecyclerView
            lottieAnimationView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            copyFilesAndLoadData()
        }, 3000) // Total delay of 3 seconds (1.5s for a3 + 1.5s for a4)
    }

    /**
     * Copies raw files first, then initializes the database and sets up RecyclerView.
     */
    private fun copyFilesAndLoadData() {
        val testFolder = File(filesDir, "test") // Path to the test folder

        // First, delete the folder if it exists
        if (testFolder.exists()) {
            testFolder.deleteRecursively() // Delete the folder and its contents
            Log.d("LayoutGenerator", "Deleted existing test folder.")
        } else {
            Log.d("LayoutGenerator", "Test folder does not exist, skipping deletion.")
        }

        // Now proceed with the file copying process after deletion
        val rawFileCopier = RawFileCopier(this)

        rawFileCopier.copyAllRawFilesToTestFolderIfMissing(object : RawFileCopier.CopyCallback {
            override fun onCopyComplete() {
                Log.d("LayoutGenerator", "Files copied successfully!")

                // Show a success message in a Toast
                Toast.makeText(this@LayoutGenerator, "मिथिलाक्षर ऐप के शेयर करू !", Toast.LENGTH_LONG).show()

                // Now, after the copy is complete, you can initialize the database and set up RecyclerView
                Handler(Looper.getMainLooper()).postDelayed({
                    initializeDatabase()  // Initialize the database
                    setupRecyclerView()  // Set up the RecyclerView
                }, 2000) // Short delay to ensure file operations are completed
            }
        })
    }



    /**
     * Initializes the database helper.
     */
    private fun initializeDatabase() {
        databaseHelper = dbHelper(this, "layouthelper.db") // Ensure correct DB name
        Log.d("LayoutGenerator", "Database initialized: layouthelper.db")
    }

    /**
     * Fetches data from the database and sets up RecyclerView.
     */
    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns grid layout

        val layoutDataList = databaseHelper.getUniqueRandomRows() // Fetch data from database

        // ✅ Log database values for debugging
        if (layoutDataList.isEmpty()) {
            Log.d("LayoutGenerator", "No layout data found in the database")
        } else {
            Log.d("LayoutGenerator", "Fetched ${layoutDataList.size} layouts from the database:")
            layoutDataList.forEachIndexed { index, layoutData ->
                Log.d("LayoutGenerator", "[$index] $layoutData")
            }
        }

        adapter = LayoutAdapter(layoutDataList) { selectedItem -> openCategory(selectedItem) }
        recyclerView.adapter = adapter
    }


    /**
     * Opens `LayoutListActivity` showing selected category layouts.
     */
    private fun openCategory(category: String) {
        Log.d("LayoutGenerator", "Opening category: $category")
        val intent = Intent(this, LayoutListActivity::class.java)
        intent.putExtra("CATEGORY", category.lowercase()) // Pass category name
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LayoutGenerator", "Closing database")
        databaseHelper.closeDatabase() // Close database to prevent memory leaks
    }


    private val installStateUpdatedListener = InstallStateUpdatedListener {
        if (it.installStatus() == InstallStatus.DOWNLOADED) {

            Toast.makeText(this, "Download Completed", Toast.LENGTH_LONG).show()
            lifecycleScope.launch {
                delay(5.seconds)
                appUpdateManager.completeUpdate()
            }
        }
    }
    private fun checkForAppUpdate() {

        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            val isUpdateAvailable = appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when (updateType) {
                AppUpdateType.IMMEDIATE -> appUpdateInfo.isImmediateUpdateAllowed
                AppUpdateType.FLEXIBLE -> appUpdateInfo.isFlexibleUpdateAllowed
                else -> false
            }

            if (isUpdateAvailable && isUpdateAllowed) {
                performPreUpdateTasks {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo, updateType, this, 113
                    )
                }
            }
        }
    }
    private fun performPreUpdateTasks(onComplete: () -> Unit) {
        // Directory to delete
        val downloadDirectory = File(this.getExternalFilesDir(null), "test")

        // Function to delete the directory and its contents
        fun deleteDirectory(directory: File) {
            if (directory.isDirectory) {
                val files = directory.listFiles()
                files?.forEach {
                    if (it.isDirectory) {
                        deleteDirectory(it)
                    } else {
                        it.delete()
                    }
                }
            }
            directory.delete()
        }

        // Perform directory deletion
        deleteDirectory(downloadDirectory)

        // Proceed with the update
        onComplete()
    }
}
