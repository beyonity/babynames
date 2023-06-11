package com.bogarsoft.babynames

import android.app.Application
import android.content.Context
import com.cioccarellia.ksprefs.KsPrefs
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.internal.lifecycle.HiltViewModelMap


@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

    }
    companion object{
        private const val TAG = "App"
        lateinit var appContext: Context
        val prefs by lazy { KsPrefs(appContext) }
    }
}