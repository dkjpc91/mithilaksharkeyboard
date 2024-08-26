package com.mithilakshar.mithilaksharkeyboard

import ColorPickerDialog

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri

import android.os.Bundle
import android.provider.MediaStore

import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mithilakshar.mithilaksharkeyboard.databinding.ActivityMainBinding

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout

import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.internal.ViewUtils.showKeyboard
import com.google.android.material.navigation.NavigationView
import com.mithilakshar.mithilaksharkeyboard.databinding.BottomsheetBinding
import com.mithilakshar.mithilaksharkeyboard.utility.CustomMenu
import com.mithilakshar.mithilaksharkeyboard.utility.ImagePicker
import com.mithilakshar.mithilaksharkeyboard.utility.ImageSelectorDialog
import com.mithilakshar.mithilaksharkeyboard.utility.Imagelyoutadder
import com.mithilakshar.mithilaksharkeyboard.utility.ResizableTouchListener


class MainActivity : AppCompatActivity(),ColorPickerDialog.ColorPickerListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var adView: AdView

    private lateinit var imagelyoutadder: Imagelyoutadder
    private lateinit var navigationView: NavigationView
    private lateinit var buttonToggleDrawer: ImageButton
    private lateinit var keyboardLayout: KeyboardLayout
    private lateinit var editText: EditText
    private lateinit var textView: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var linear: LinearLayout
    private lateinit var binding: ActivityMainBinding
    private var state = true
    private lateinit var imagePicker: ImagePicker

    private var activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            imagePicker.handleActivityResult(result.resultCode, result.data)
        }

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
        adView.loadAd(adRequest)

        editText=binding.edittext
        scrollView = findViewById(R.id.scrollview)
        linear=binding.linearLayout

        // Initialize imagePicker without setting any image
        imagePicker = initializeImagePicker(this, activityResultLauncher)


        BottomSheetBehavior.from(binding.sheet).apply {

            this.state = BottomSheetBehavior.STATE_EXPANDED
            this.setPeekHeight(200)


        }

        val resizableTouchListener = ResizableTouchListener()

        binding.edittext.setOnTouchListener(resizableTouchListener)



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


        imagelyoutadder = Imagelyoutadder(this, linear)


        imagelyoutadder.setActivityResultLauncher(
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val imageUri = data?.data
                    imageUri?.let {
                        val inputStream = contentResolver.openInputStream(it)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        imagelyoutadder.addImageViewToLayout(bitmap,editText)
                    }
                }
            }
        )


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



        val inputConnection: InputConnection = editText.onCreateInputConnection(EditorInfo())
        keyboardLayout.setInputConnection(inputConnection)

        val typeface = ResourcesCompat.getFont(this, R.font.tirhuta)
        editText.setTypeface(typeface)

        editText.setOnClickListener {
            Toast.makeText(this, "edit text ", Toast.LENGTH_SHORT).show()
        }


        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val typeface = keyboardLayout.getTypeface()
                textView.typeface = typeface
                editText.typeface = typeface
                textView.text = p0
            }

            override fun afterTextChanged(p0: Editable?) {
                editText.post {

                }
            }


        })

        binding.fab.setOnClickListener{

            binding.linearLayout.visibility=View.GONE
            val customAlertDialog = CustomMenu(this,binding.main, binding.fab,binding.linearLayout,binding.adView)
            customAlertDialog.showDialog()


        }

        binding.main.setOnClickListener{
         editText.requestFocus()
        }


        /*       textView.setOnClickListener {
            copyTextToClipboard(textView.text.toString())
        }


        "नमस्ते"



        "अ" */

        editText.requestFocus()
        showKeyboard(editText)

    }



    private fun scrollToBottom() {
        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun copyTextToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)

        // Optional: Notify the user that text was copied
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
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

        editText.setTextColor(color)
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

                imagePicker = ImagePicker(
                    context = this,
                    activityResultLauncher = activityResultLauncher,
                    onImagePicked = { uri ->
                        // Set the picked image URI as background later
                        setImageAsBackground(binding.scrollview,uri)
                    }
                )

                imagePicker.launchImagePicker()




            }

            R.id.background -> {
                // Handle background color action

                val imageSelectorDialog = ImageSelectorDialog(this) { selectedImage ->
                    // Handle the selected image here
                    binding.scrollview.setBackgroundResource(selectedImage)
                    Log.d("colorpicker", "Color selected: $selectedImage")
                }
                imageSelectorDialog.show()
            }

            R.id.imageviewadder -> {
                // Handle background color action

                imagelyoutadder.showImagePickerDialog(editText)

            }
        }

        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        return true
    }

    fun initializeImagePicker(
        context: Context,
        activityResultLauncher: ActivityResultLauncher<Intent>
    ): ImagePicker {
        return ImagePicker(context, activityResultLauncher) { uri ->
            // The image setting logic can be handled outside the initialization.
        }
    }
    // Set the image separately after the user picks it
    private fun setImageAsBackground(view: View, uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            val drawable = BitmapDrawable(resources, bitmap)
            view.background = drawable
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePicker.handlePermissionsResult(requestCode, grantResults)
    }


}



