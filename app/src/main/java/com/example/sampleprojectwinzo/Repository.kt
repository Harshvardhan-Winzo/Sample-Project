package com.example.sampleprojectwinzo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Repository(private val languageDao: LanguageDao) {

    var languageList: List<Language> = emptyList()

    suspend fun getAllLanguages(): List<Language> {
        return languageDao.getAll()
    }

    suspend fun insertLanguages(languages: List<Language>) {
        languageDao.insertAll(languages)
    }

    suspend fun fetchLanguagesFromApi(context: Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiService::class.java)
        try {
            val languageData = service.getData()
            val languageList = languageData.languageData

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "API call successful: ${languageList.size} items",
                    Toast.LENGTH_SHORT
                ).show()
            }
            withContext(Dispatchers.IO) {
                insertLanguages(languageList)
            }
        } catch (e: Exception) {
            return
        }
    }

}