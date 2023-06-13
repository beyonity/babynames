package com.bogarsoft.babynames.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.bogarsoft.babynames.BuildConfig
import com.bogarsoft.babynames.R
import com.bogarsoft.babynames.databinding.ActivityAboutBinding
import com.bogarsoft.babynames.utils.Constants
import com.bogarsoft.babynames.utils.ImageCreditData
import com.google.android.material.chip.Chip

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            //showing dialog and then closing the application..
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this@AboutActivity, onBackPressedCallback)
        binding.toolbar.title.text = getString(R.string.about)
        binding.toolbar.subtitle.text = getString(R.string.app_name)
        binding.toolbar.back.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        binding.version.text = BuildConfig.VERSION_NAME
        ImageCreditData.getList().forEach { image ->
            val chip = Chip(this@AboutActivity)
            chip.text = image.name
            chip.isClickable = true
            chip.isCheckable = false
            chip.setOnClickListener {
                //open url with intent
                val intent = Intent(this,WebViewActivity::class.java).apply {
                    putExtra("url",image.url)
                }
                startActivity(intent)

            }
            binding.imagecredits.addView(chip)
        }

        binding.privacypolicycard.setOnClickListener {

            //OPEN URL WITH INTENT
            val intent = Intent(this,WebViewActivity::class.java).apply {
                putExtra("url", Constants.PRIVACY_URL)
            }
            startActivity(intent)
        }

        binding.termscard.setOnClickListener {
            val intent = Intent(this,WebViewActivity::class.java).apply {
                putExtra("url",Constants.TERMS_URL)
            }
            startActivity(intent)
        }

        binding.developercard.setOnClickListener {
            //open email
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + Constants.OFFICIAL_EMAIL))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Business Enquiry")
            startActivity(intent)

        }

        binding.telegramcard.setOnClickListener {
            //open telegram
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TELEGRAM_URL))
            startActivity(intent)
        }

    }
}