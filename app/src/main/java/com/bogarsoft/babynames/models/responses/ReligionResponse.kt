package com.bogarsoft.babynames.models.responses

import com.bogarsoft.babynames.models.global.Religion

data class ReligionResponse(val error:Boolean,val message:String,val data:List<Religion>)