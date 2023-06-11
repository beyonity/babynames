package com.bogarsoft.babynames.activities

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import com.bogarsoft.babynames.utils.StorageUtil
import com.bogarsoft.babynames.utils.ContextUtils

open class BaseActivity: AppCompatActivity() {

   override fun attachBaseContext(newBase: Context) {
// get chosen language from shread preference
    val localeToSwitchTo = StorageUtil(newBase).preferredLanguage
    val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeToSwitchTo)
    super.attachBaseContext(localeUpdatedContext)
}

}