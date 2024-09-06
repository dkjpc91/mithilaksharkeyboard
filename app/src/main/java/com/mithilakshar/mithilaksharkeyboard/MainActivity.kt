package com.mithilakshar.mithilaksharkeyboard

import ColorPickerDialog
import PermissionManager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build

import android.os.Bundle
import android.provider.MediaStore

import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mithilakshar.mithilaksharkeyboard.databinding.ActivityMainBinding

import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View

import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.mithilakshar.mithilaksharkeyboard.databinding.BottomsheetBinding
import com.mithilakshar.mithilaksharkeyboard.utility.CustomMenu
import com.mithilakshar.mithilaksharkeyboard.utility.ImagePicker
import com.mithilakshar.mithilaksharkeyboard.utility.ImageSelectorDialog
import com.mithilakshar.mithilaksharkeyboard.utility.Imagelyoutadder
import com.mithilakshar.mithilaksharkeyboard.utility.TextViewAdder


class MainActivity : AppCompatActivity(),ColorPickerDialog.ColorPickerListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var adView: AdView

    private lateinit var textViewAdder: TextViewAdder


    private lateinit var navigationView: NavigationView
    private lateinit var buttonToggleDrawer: ImageButton
    private lateinit var keyboardLayout: KeyboardLayout
    private lateinit var textView: TextView
    private lateinit var linear: RelativeLayout
    private lateinit var binding: ActivityMainBinding
    private var state = true
    private lateinit var imagePicker: ImagePicker
    private lateinit var imagelyoutadder: Imagelyoutadder
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var permissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        MobileAds.initialize(this) {}
        adView = binding.adView
        val adRequest = AdRequest.Builder().build()
        //adView.loadAd(adRequest)

        linear=binding.relative
        textViewAdder = TextViewAdder(this,linear,true)




        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            imagePicker.handleActivityResult(result.resultCode, result.data)
        }



        imagePicker = ImagePicker(this, activityResultLauncher) { uri ->
            setImageAsBackground(binding.relative,uri)
        }





        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    imagelyoutadder.addImageViewToLayout(bitmap, binding.relative)
                }
            }
        }

        imagelyoutadder = Imagelyoutadder(
            context = this,
            parentLayout =  binding.relative,
            activityResultLauncher = activityResultLauncher,
            onImagePicked = { uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                imagelyoutadder.addImageViewToLayout(bitmap,  binding.relative)
            }
        )



        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Handle the permission granted case
            } else {
                // Handle the permission denied case
            }
        }








        BottomSheetBehavior.from(binding.sheet).apply {

            this.state = BottomSheetBehavior.STATE_EXPANDED
            this.setPeekHeight(200)


        }










        drawerLayout = findViewById(R.id.drawer_layout)
        buttonToggleDrawer = binding.drawertoggle

        navigationView = findViewById(R.id.nav_view)
        setupCustomMenu()

        navigationView.setNavigationItemSelectedListener(this)


        buttonToggleDrawer.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }







        textView = binding.textview
        val bottomSheet = binding.sheet
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val includedLayoutBinding = BottomsheetBinding.bind(bottomSheet)

        includedLayoutBinding.expand.setOnClickListener {

            when (state) {
                false -> {
                    state = true
                    includedLayoutBinding.expand.setImageResource(R.drawable.zigzag)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }

                true -> {
                    includedLayoutBinding.expand.setImageResource(R.drawable.up)
                    state = false
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }

            }


        }




        keyboardLayout = includedLayoutBinding.mithilaksharkeyboard








        binding.fab.setOnClickListener{

           // binding.linearLayout.visibility=View.GONE
            val customAlertDialog = CustomMenu(this,binding.relative,imagePicker,imagelyoutadder,permissionLauncher)
            customAlertDialog.showDialog()


        }

        binding.main.setOnClickListener{

        }


        /*       textView.setOnClickListener {
            copyTextToClipboard(textView.text.toString())
        }


        "नमस्ते"



        "अ" */



    }






    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    private fun setupCustomMenu() {
        val menu = navigationView.menu
        val menuInflater = menuInflater

        // Inflate a default menu to get the menu items
        menuInflater.inflate(R.menu.drawer_menu, menu)

        // Customize each menu item
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)

            // Create a custom view for the menu item
            val customView = LayoutInflater.from(this).inflate(R.layout.menu_item_custom, null)

            // Set the icon and title
            val iconView = customView.findViewById<ImageView>(R.id.menuicon)
            val titleView = customView.findViewById<TextView>(R.id.menutitle)

            when (menuItem.itemId) {
                R.id.fontcolor -> {
                   iconView.setImageResource(R.drawable.palette)
                   titleView.text = "रंगक चुनाव करू"
                }

                R.id.background -> {
                iconView.setImageResource(R.drawable.palette)
                titleView.text = "बैकग्राउंड के चुनाव करू "
            }
                R.id.mobileimage -> {
                    iconView.setImageResource(R.drawable.palette)
                    titleView.text = "अपन मनपसंद बैकग्राउंड चुनू "
                }

                R.id.imageviewadder -> {
                    iconView.setImageResource(R.drawable.palette)
                    titleView.text = "image चुनू "
                }
                R.id.textadder -> {
                    iconView.setImageResource(R.drawable.palette)
                    titleView.text = "text चुनू "
                }
            }
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.bottomMargin = 48 // Add margin programmatically

            customView.layoutParams = layoutParams

            menuItem.actionView = customView
        }
    }








    override fun onColorSelected(color: Int) {
        // Handle the selected color


        Toast.makeText(this, "रंगक चुनाव पूर्ण भेल : ${String.format("#%06X", (0xFFFFFF and color))}", Toast.LENGTH_LONG).show()
        Log.d("colorpicker", "Color selected: $color")
    }


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.fontcolor -> {
                val colorPickerDialog = ColorPickerDialog(this, this)
                colorPickerDialog.show()
                Log.d("colorpicker", "Color picker dialog shown")
            }
            R.id.mobileimage -> {




            }

            R.id.background -> {
                // Handle background color action


            }

            R.id.imageviewadder -> {
                // Handle background color action

              // imagelyoutadder.showImagePickerDialog(editText)

            }

            R.id.textadder -> {
                // Handle background color action

                textViewAdder.showTextInputDialog()

            }
        }

        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        return true
    }




/*    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePicker.handlePermissionsResult(requestCode, grantResults)
    }*/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Let PermissionHandler manage the permission result
        PermissionManager(this).handlePermissionResult(requestCode, grantResults)
    }


    private fun setImageAsBackground(view: View, imageUri: Uri) {
        val imageStream = contentResolver.openInputStream(imageUri)
        val drawable = Drawable.createFromStream(imageStream, imageUri.toString())
        view.background = drawable
    }


}



