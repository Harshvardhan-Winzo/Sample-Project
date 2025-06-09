package com.example.sampleprojectwinzo

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Language::class], version = 1)
abstract class LanguageDatabase : RoomDatabase() {
    abstract fun LanguageDao(): LanguageDao
}