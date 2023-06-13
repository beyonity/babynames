package com.bogarsoft.babynames.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.bogarsoft.babynames.BuildConfig
import com.bogarsoft.babynames.R
import com.bogarsoft.babynames.adapters.PageAdapter
import com.bogarsoft.babynames.databinding.ActivityMainBinding
import com.bogarsoft.babynames.fragments.HomeFragment
import com.bogarsoft.babynames.fragments.RasiCalculatorFragment
import com.bogarsoft.babynames.fragments.FavoriteFragment
import com.bogarsoft.babynames.viewModels.MainViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bogarsoft.babynames.utils.Helper
import com.bogarsoft.babynames.utils.StorageUtil
import com.bogarsoft.babynames.utils.hide
import com.bogarsoft.babynames.utils.show

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var pageAdapter: PageAdapter

    private var pressedTime: Long = 0
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            //showing dialog and then closing the application..
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                finish()
            } else {
                //Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT).show()
                Snackbar.make(binding.root,"Press back again to exit", Snackbar.LENGTH_SHORT).show()
            }
            pressedTime = System.currentTimeMillis()
        }
    }

    private lateinit var adView: AdView
    private lateinit var adContainerView: FrameLayout
    private var initialLayoutComplete = false
    private val adSize: AdSize
        @RequiresApi(Build.VERSION_CODES.R)
        get() {
            val windowMetrics = windowManager.currentWindowMetrics
            val bounds = windowMetrics.bounds

            var adWidthPixels = adContainerView.width.toFloat()

            // If the ad hasn't been laid out, default to the full screen width.
            if (adWidthPixels == 0f) {
                adWidthPixels = bounds.width().toFloat()
            }


            val density = resources.displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()

            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }


    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adContainerView = binding.adcontainer
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        MobileAds.initialize(this) {}
        adView = AdView(this)
        adContainerView.addView(adView)
        adContainerView.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadBanner()
            }
        }
        viewModel.hideBottomNav.observe(this, Observer {
            if(it){
                //hide with animation slide down animation
                binding.bottomNavigation.animate().translationY(binding.bottomNavigation.getHeight()
                    .toFloat()).duration = 200;
                binding.bottomNavigation.hide()



            }else{
                binding.bottomNavigation.show()
                binding.bottomNavigation.animate().translationY(0F).duration = 200;

            }
        })

        viewModel.appupdate.observe(this, Observer { update ->
            Log.d(TAG, "onCreate: $update")
            val currversion  = BuildConfig.VERSION_CODE
            Log.d(TAG, "onCreate: $currversion")

            val difference = update.code - currversion
            Log.d(TAG, "onCreate: $difference")
            if (difference >=10){
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Update Available")
                builder.setCancelable(false)
                builder.setMessage("A new Version ${update.name} is available now, Your app version is outdated please udpate now")

                builder.setPositiveButton("Update Now!",
                    DialogInterface.OnClickListener { dialog, which -> gotoplaystore() })

                if (BuildConfig.DEBUG) {
                    builder.show()
                } else {
                    builder.show()
                }
            }else if (difference in 1..9) {
                val cancelcount = StorageUtil(this).updateCancelCount
                if(cancelcount < 3 && !Helper.isUpdateDialogShown()){
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)

                    builder.setTitle("Update Available")

                    builder.setCancelable(false)
                    builder.setMessage("A new Version ${update.name} is available now, Your app version is outdated please udpate now")

                    builder.setPositiveButton("Update Now!",
                        DialogInterface.OnClickListener { dialog, which -> gotoplaystore() })

                    builder.setNeutralButton("Later",
                        DialogInterface.OnClickListener { dialog, which ->
                            StorageUtil(this).updateCancelCount = cancelcount + 1
                            dialog.dismiss()
                        })

                    if (BuildConfig.DEBUG) {
                        builder.show()
                    } else {
                        builder.show()
                    }
                }
            }else {
                StorageUtil(this).updateCancelCount = 0
            }
        })
        Handler(Looper.getMainLooper()).postDelayed({
            checkAppUpdate()
        }, 1000)
        askNotificationPermission()


        init()
    }

    private fun checkAppUpdate(){
        viewModel.getAppUpdate()
    }

    private fun loadBanner() {
        adView.adUnitId = if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/6300978111" else getString(R.string.admob_above_bottomnav_ad_unit)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            adView.setAdSize(adSize)
        }

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this device."
        val adRequest = AdRequest.Builder().build()
        // Start loading the ad in the background.
        adView.loadAd(adRequest)
    }
    private fun init(){
        pageAdapter= PageAdapter(supportFragmentManager)
        pageAdapter.addFragment(HomeFragment(),"Home")
        pageAdapter.addFragment(RasiCalculatorFragment(),"Page One")
        pageAdapter.addFragment(FavoriteFragment(),"Page Two")
        binding.viewpager.offscreenPageLimit=5
        binding.viewpager.adapter=pageAdapter
        binding.viewpager.disableScroll(true)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home ->{
                    binding.viewpager.setCurrentItem(0,false)
                    true
                }
                R.id.pageone ->{
                    binding.viewpager.setCurrentItem(1,false)
                    true
                }

                R.id.pagetwo -> {
                    binding.viewpager.setCurrentItem(2, false)
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun gotoplaystore() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // FCM SDK won't deliver notifications to this app via FCM.
        }
    }

    companion object{
        private const val TAG = "MainActivity"
    }
}