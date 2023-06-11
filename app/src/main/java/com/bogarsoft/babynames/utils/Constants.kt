package com.bogarsoft.babynames.utils


object Constants{
    val DOMAIN: String = "https://tamillyrics.page.link"
    val PRIVACY_URL = "https://tamillyrics.bogarsoft.com/android/privacy.html"
    val TERMS_URL = "https://tamillyrics.bogarsoft.com/android/terms.html"
    val TELEGRAM_URL = "https://t.me/tamilhits"
    val OFFICIAL_EMAIL = "contactus@bogarsoft.com"
    val DMCA_EMAIL = "lyrics@bogarsoft.com"
    const val THEME_MODE = "theme_mode"
    const val LIST_VIEW = 1
    const val AD_VIEW = 2
    const val LOADING_VIEW = 3
    const val NAME_VIEW_TYPE = "NAME VIEW"
    const val SONG_VIEW_TYPE = "SONG VIEW"
    const val LYRICS_VIEW_TYPE = "LYRICS VIEW"
    const val AD_VIEW_TYPE = "AD VIEW"


    enum class Language{
        TAMIL,ENGLISH
    }
    enum class PendingTask(){
        NONE,
        LIKE,
        FAVORITE,
        COMMENT,
        COMMENTLIKE,
        SHARE
    }

    const val COMMENT_TOP = "COMMENT_TOP"


    const val SONG_SORT_NAME_ORDER = "SONG_SORT_NAME_ORDER"
    const val SONG_SORT_YEAR_ORDER = "SONG_SORT_YEAR_ORDER"
    const val SONG_FILTER_OPTION = "SONG_FILTER_OPTION"
    const val SONG_FILTER_SINGLE_YEAR = "SONG_FILTER_SINGLE_YEAR"
    const val SONG_FILTER_YEAR_RANGE = "SONG_FILTER_YEAR_RANGE"
    const val SONG_ALPHABET_FILTER = "SONG_ALPHABET_FILTER"


    const val ALBUM_SORT_NAME_ORDER = "ALBUM_SORT_NAME_ORDER"
    const val ALBUM_SORT_YEAR_ORDER = "ALBUM_SORT_YEAR_ORDER"
    const val ALBUM_FILTER_OPTION = "ALBUM_FILTER_OPTION"
    const val ALBUM_FILTER_SINGLE_YEAR = "ALBUM_FILTER_SINGLE_YEAR"
    const val ALBUM_FILTER_YEAR_RANGE = "ALBUM_FILTER_YEAR_RANGE"
    const val ALBUM_ALPHABET_FILTER = "ALBUM_ALPHABET_FILTER"
    enum class SORT_BY{
        NAME
    }

    enum class ORDER_BY{
        ASC,
        DESC
    }

    //filter by years
   enum class FILTER_TYPE{
       SINGLE_YEAR,
       YEAR_RANGE,
   }

   val yearrange = arrayListOf<Pair<Int,Int>>(
            Pair(2020,2029),
            Pair(2010,2019),
            Pair(2000,2009),
            Pair(1990,1999),
            Pair(1980,1989),
            Pair(1970,1979),
            Pair(1960,1969),
            Pair(1950,1959),
            Pair(1940,1949),
   )

    enum class COMMENTTYPE{
        APP,
        GIPHY
    }



}