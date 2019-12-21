package com.shadhin.android_jetpack.view.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DogDao {
    @Insert
    suspend fun insertAll(vararg dogs: DogModel): List<Long>

    @Query("SELECT * FROM dogmodel")
    suspend fun getAllDogs(): List<DogModel>

    @Query("SELECT * FROM dogmodel WHERE uuid=:dogId")
    suspend fun getDog(dogId: Int): DogModel

    @Query("DELETE FROM dogmodel")
    suspend fun deleteAllDogs()
}