package com.bogarsoft.babynames.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.bogarsoft.babynames.R
import com.bogarsoft.babynames.utils.hide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textview.MaterialTextView

class WebViewActivity : AppCompatActivity() {
    lateinit var webview : WebView
    lateinit var back : MaterialButton
    lateinit var titleToolbar : MaterialTextView
    lateinit var pg : LinearProgressIndicator
    var url = ""
    var title = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        findViewById<AppBarLayout>(R.id.appbar).hide()
        url = intent.getStringExtra("url").toString()
        back = findViewById(R.id.back)
        webview = findViewById(R.id.webview)
        titleToolbar = findViewById(R.id.title)
        pg = findViewById(R.id.pg)

        back.setOnClickListener({
            onBackPressed()
        })
        webview.clearCache(true)
        webview.loadUrl(url)
        webview.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                pg.setProgress(progress)
                if (progress == 100){
                    //pg.visibility = View.GONE
                    titleToolbar.setText(webview.title)
                }
            }
        })
    }
}