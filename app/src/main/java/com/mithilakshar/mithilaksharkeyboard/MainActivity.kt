package com.mithilakshar.mithilaksharkeyboard

import ColorPickerDialog
import PermissionManager

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri

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
import android.view.WindowManager
import androidx.lifecycle.Observer
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.mithilakshar.mithilaksharkeyboard.Dialog.Networkdialog
import com.mithilakshar.mithilaksharkeyboard.Room.UpdatesDao
import com.mithilakshar.mithilaksharkeyboard.Room.UpdatesDatabase
import com.mithilakshar.mithilaksharkeyboard.databinding.BottomsheetBinding
import com.mithilakshar.mithilaksharkeyboard.utility.CustomMenu
import com.mithilakshar.mithilaksharkeyboard.utility.FirebaseFileDownloader
import com.mithilakshar.mithilaksharkeyboard.utility.ImagePicker
import com.mithilakshar.mithilaksharkeyboard.utility.Imagelyoutadder
import com.mithilakshar.mithilaksharkeyboard.utility.NetworkManager
import com.mithilakshar.mithilaksharkeyboard.utility.RewardAdManager
import com.mithilakshar.mithilaksharkeyboard.utility.TextViewAdder
import com.mithilakshar.mithilaksharkeyboard.utility.dbDownloader
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import kotlin.time.Duration.Companion.seconds


class MainActivity : AppCompatActivity(),ColorPickerDialog.ColorPickerListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var adView: AdView

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.IMMEDIATE

    private lateinit var updatesDao: UpdatesDao
    private lateinit var fileDownloader: FirebaseFileDownloader
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
    private lateinit var rewardAdManager: RewardAdManager

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

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

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

        rewardAdManager = RewardAdManager(this)
        rewardAdManager.initialize()

        MobileAds.initialize(this) {}
        adView = binding.adView
        val adRequest = AdRequest.Builder().build()
        //adView.loadAd(adRequest)

        linear=binding.relative
        textViewAdder = TextViewAdder(this,linear,true)



        fileDownloader = FirebaseFileDownloader(this)
        updatesDao = UpdatesDatabase.getDatabase(applicationContext).UpdatesDao()

        val dbDownloader= dbDownloader(updatesDao,fileDownloader)

        dbDownloader.observeFileExistence("Imageslist",this,lifecycleScope,1,this)

        updatesDao.getUniqueStringById(2).observe(this, Observer { uniqueString ->



        })



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
            val customAlertDialog = CustomMenu(this,binding.relative,imagePicker,imagelyoutadder,updatesDao,rewardAdManager)
            customAlertDialog.showDialog()


        }



        /*       textView.setOnClickListener {
            copyTextToClipboard(textView.text.toString())
        }


        "नमस्ते"



        "अ" */



    }

    //updateappstart.

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

    //updateappstop.



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
                   titleView.text = "ऐप शेयर करू "
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
                val shareText = "नीचा देल गेल लिंक पर क्लिक क मिथिलाक्षर पोस्टर ऐप्प डाउनलोड करू .\n https://play.google.com/store/apps/details?id=${this.packageName} \n\n\n @mithilakshar13"

                // Create the intent to share the text
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }

                // Start the sharing activity
                startActivity(Intent.createChooser(intent, "साझा करू : "))
            }




        }

        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        return true
    }




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



