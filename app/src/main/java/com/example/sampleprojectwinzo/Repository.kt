package com.example.sampleprojectwinzo

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.core.content.edit
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository(application: Application) : Thread() {

    private val context: Context = application.applicationContext

    private val languageDao = Room.databaseBuilder(context, LanguageDatabase::class.java, "database-name").build().LanguageDao()

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("LanguagePrefs", Context.MODE_PRIVATE)

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://run.mocky.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(ApiService::class.java)

    fun getsharedPrefsFile() : SharedPreferences{
        return sharedPrefs
    }

    fun writeToSharedPref(fileName :String, languageId: String){
        sharedPrefs.edit{putString(fileName, languageId)}
    }

    suspend fun getAllLanguages(): List<Language> {
        return languageDao.getAll()
    }

    private suspend fun insertLanguages(languages: List<Language>) {
        languageDao.insertAll(languages)
    }

    suspend fun fetchLanguagesFromApi(context: Context) {
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