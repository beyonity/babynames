package com.bogarsoft.babynames.models.responses

import com.bogarsoft.babynames.models.global.Naksathra

data class NakshatraResponse(val error:Boolean, val message:String, val data:ArrayList<Naksathra>)