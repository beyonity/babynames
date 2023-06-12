package com.bogarsoft.babynames.utils

import com.bogarsoft.babynames.BuildConfig

class Helper {

    companion object{

        fun getImage(name:String):String{
            val BASE_URL = if(BuildConfig.DEBUG) BuildConfig.DEBUG_URL else BuildConfig.APP_URL
            val IMAGE_URL = BASE_URL.replace("api/v1/user/","images")
            return "$IMAGE_URL/$name"

        }
    }
}