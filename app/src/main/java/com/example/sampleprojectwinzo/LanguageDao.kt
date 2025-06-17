package com.example.sampleprojectwinzo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LanguageDao {
    @Query("SELECT * FROM language")
    suspend fun getAll(): List<Language>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(languages: List<Language>)

}