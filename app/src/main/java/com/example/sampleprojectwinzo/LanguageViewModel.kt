package com.example.sampleprojectwinzo

import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LanguageViewModel(
    private val repository: Repository
) : ViewModel() {

    private var lastClickedLanguageId: String? = null
    private var currentSelectedLanguage: Language? = null

    private val _languageList = MutableLiveData<List<Language>>()
    val languageList: LiveData<List<Language>> get() = _languageList

    fun fetchLanguages(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d(null,"in fetchLang")
                val isMainThread = (Looper.getMainLooper() == Looper.myLooper())
                Log.d(null, if (isMainThread) "Yes it's main thread" else "Not main thread")
                repository.fetchLanguagesFromApi(context.applicationContext)
                val languages = repository.getAllLanguages()
                _languageList.postValue(languages)
            }
        }
    }

    fun getcurrentSelectedLanguage() : Language? {
        return currentSelectedLanguage
    }

    fun setcurrentSelectedLanguage(language: Language?){
         currentSelectedLanguage = language
    }

    fun writeToSP(fileName :String, languageId: String){
        repository.writeToSharedPref(fileName,languageId)
    }

    fun setlastClickedLanguageId() {
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                lastClickedLanguageId = repository.getsharedPrefsFile().getString("clicked_language_id", null)
            }
        }
    }

    fun getlastClickedLanguageId(): String? {
        return lastClickedLanguageId
    }
}
