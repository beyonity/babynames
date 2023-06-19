package com.bogarsoft.babynames.utils

import android.widget.EditText
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

        //check for empty editext and show error
        fun checkEmptyEditText(vararg editTexts:EditText):Boolean{
            var isEmpty = false
            for(editText in editTexts){
                if(editText.text.toString().isEmpty()){
                    editText.error = editText.hint.toString()
                    isEmpty = true
                }
            }
            return isEmpty
        }
    }
}