package com.bogarsoft.babynames.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bogarsoft.babynames.database.dao.FavoriteNameDao
import com.bogarsoft.babynames.models.local.FavoriteName


@Database(entities = [FavoriteName::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun favoriteNameDao(): FavoriteNameDao
    companion object{
        val DATA_BASE_NAME = "app_database"
    }
}