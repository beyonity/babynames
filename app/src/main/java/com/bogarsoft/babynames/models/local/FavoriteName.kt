package com.bogarsoft.babynames.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_name")
data class FavoriteName (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val babyId:Int,
)