package com.bogarsoft.babynames.models.responses

import com.bogarsoft.babynames.models.global.BabyName

data class BabyNameResponse(val error:Boolean,val message:String,val data:ArrayList<BabyName>)
