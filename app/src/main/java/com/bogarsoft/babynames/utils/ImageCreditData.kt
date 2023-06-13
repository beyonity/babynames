package com.bogarsoft.babynames.utils

import com.bogarsoft.babynames.models.local.ImageCredit

class ImageCreditData {

    companion object{
        fun getList():ArrayList<ImageCredit>{
            return arrayListOf(
                //ImageCredit("Google Inc","https://iconscout.com/contributors/google-inc"),
                //ImageCredit("Jemis Mali","https://iconscout.com/contributors/jemismali"),
                //ImageCredit("Ladalle Studio","https://iconscout.com/contributors/ladalle-cs"),
                //ImageCredit("Poly Artboard","https://iconscout.com/contributors/polyartboard"),
                ImageCredit("Flaticon","https://www.flaticon.com/free-icons/"),
                //ImageCredit("Unicons Font","https://iconscout.com/contributors/unicons"),
                //ImageCredit("The Icon Z","https://iconscout.com/contributors/theiconz"),
                //ImageCredit("IconScout Store","https://iconscout.com/contributors/iconscout"),
                ImageCredit("Freepik","https://www.freepik.com"),
                //ImageCredit("Icon8","https://icons8.com"),
            )
        }
    }
}