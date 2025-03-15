package com.mithilakshar.mithilaksharkeyboard.UI

import com.mithilakshar.mithilaksharkeyboard.UI.DisplayLayoutActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mithilakshar.mithilaksharkeyboard.R
import com.mithilakshar.mithilaksharkeyboard.adapter.LayoutListAdapter
import com.mithilakshar.mithilaksharkeyboard.utility.dbHelper

class LayoutListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LayoutListAdapter
    private lateinit var databaseHelper: dbHelper // SQLite Database Helper
    private var layoutDataList: List<Map<String, Any?>> = emptyList() // Data from DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_layout_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // ✅ Grid layout with 2 columns

        // ✅ Get category name from Intent
        val category = intent.getStringExtra("CATEGORY")
        if (category.isNullOrEmpty()) {
            Log.e("LayoutListActivity", "No category provided!")
            finish()
            return
        }

        // ✅ Initialize database and fetch layouts
        databaseHelper = dbHelper(this, "layouthelper.db")
        fetchLayoutsFromDB(category)
    }

    /**
     * Fetch layouts from SQLite where "name" column matches the category.
     */
    private fun fetchLayoutsFromDB(category: String) {
        layoutDataList = databaseHelper.getLayoutDataByName(category) // Fetch layouts from DB

        if (layoutDataList.isEmpty()) {
            Log.w("LayoutListActivity", "No layouts found for category: $category")
        } else {
            Log.d("LayoutListActivity", "Fetched ${layoutDataList.size} layouts for category: $category")
            layoutDataList.forEachIndexed { index, layoutData ->
                Log.d("LayoutListActivity", "[$index] $layoutData")
            }
        }

        setupRecyclerView()
    }

    /**
     * Sets up RecyclerView after getting data.
     */
    private fun setupRecyclerView() {
        adapter = LayoutListAdapter(layoutDataList) { selectedItem ->
            openLayout(selectedItem) // Pass the full map instead of just the name
        }
        recyclerView.adapter = adapter
    }

    /**
     * Opens `DisplayLayoutActivity` with all selected layout data.
     */
    private fun openLayout(layoutData: Map<String, Any?>) {
        val intent = Intent(this, DisplayLayoutActivity::class.java)
        val bundle = Bundle()

        // Store all map values in the bundle
        layoutData.forEach { (key, value) ->
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                is Double -> bundle.putDouble(key, value)
                is Float -> bundle.putFloat(key, value)
            }
        }

        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHelper.closeDatabase() // ✅ Close DB to prevent memory leaks
    }
}
