package com.bogarsoft.babynames.models.responses

import com.bogarsoft.babynames.models.global.Rashi

data class RashiResponse(val error:Boolean, val message:String, val data:ArrayList<Rashi>)