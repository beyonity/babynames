package com.bogarsoft.babynames.models.local

data class MenuItem(val id:Int, val icon:String, val title:String,var letters:String = "", var isSelected:Boolean = false)
