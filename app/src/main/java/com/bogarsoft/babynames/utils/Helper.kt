package com.bogarsoft.babynames.utils

import com.bogarsoft.babynames.BuildConfig

class Helper {

    companion object{
        private var isUpdateDialogShown = false

        fun setUpdateDialogShown(value:Boolean){
            isUpdateDialogShown = value
        }
        fun isUpdateDialogShown():Boolean{
            return isUpdateDialogShown
        }
        fun getImage(name:String):String{
            val BASE_URL = if(BuildConfig.DEBUG) BuildConfig.DEBUG_URL else BuildConfig.APP_URL
            val IMAGE_URL = BASE_URL.replace("api/v1/user/","images")
            return "$IMAGE_URL/$name"

        }
    }
}