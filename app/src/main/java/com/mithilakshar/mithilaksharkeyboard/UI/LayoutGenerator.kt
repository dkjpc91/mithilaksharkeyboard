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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.mithilakshar.mithilaksharkeyboard.MainActivity
import com.mithilakshar.mithilaksharkeyboard.R
import com.mithilakshar.mithilaksharkeyboard.adapter.LayoutAdapter
import com.mithilakshar.mithilaksharkeyboard.databinding.ActivityLayoutGeneratorBinding
import com.mithilakshar.mithilaksharkeyboard.utility.dbHelper
import java.io.File

class LayoutGenerator : AppCompatActivity() {

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

        Log.d("LayoutGenerator", "Starting file copy process")
        recyclerView = findViewById(R.id.recyclerView)
        lottieAnimationView = binding.lottieAnimationView

        showLottieAnimation()

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



    }

    private fun showLottieAnimation() {
        recyclerView.visibility = View.GONE
        lottieAnimationView.playAnimation() // Start the Lottie animation
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
}
