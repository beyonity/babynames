package com.bogarsoft.babynames.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bogarsoft.babynames.models.local.FavoriteName


@Dao
interface FavoriteNameDao{

    @Query("SELECT * FROM favorite_name")
    fun getAll(): List<FavoriteName>

    @Query("SELECT * FROM favorite_name WHERE id = :id")
    fun getById(id: Int): FavoriteName?

    @Query("SELECT * FROM favorite_name WHERE babyId = :babyId")
    fun getByBabyId(babyId: Int): FavoriteName?

    @Insert
    fun insert(favoriteName: FavoriteName)

    @Query("DELETE FROM favorite_name WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM favorite_name WHERE babyId = :babyId")
    fun deleteByBabyId(babyId: Int)

    @Query("DELETE FROM favorite_name")
    fun deleteAll()


}