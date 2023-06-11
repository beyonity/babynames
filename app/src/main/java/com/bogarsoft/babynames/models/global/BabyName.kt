package com.bogarsoft.babynames.models.global

data class BabyName(val id:Int,val english:String,val meaning:String,val gender:String,val isTop100:Boolean,val easyToPronounce:Boolean,val religion:Religion,val modernName:Boolean,val shortandSweet:Boolean,val rashi:Rashi?=null,val naksathra:Naksathra?=null,val isFavourite:Boolean,val numerology:String)