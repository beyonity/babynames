package com.bogarsoft.babynames.models.responses

import com.bogarsoft.babynames.models.global.AppUpdate


data class AppUpdateResponse(val error:Boolean,val message:String,val data: AppUpdate)