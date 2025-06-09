package com.example.sampleprojectwinzo

import android.app.Application
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LanguageViewModel(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    private val _languageList = MutableLiveData<List<Language>>()
    val languageList: LiveData<List<Language>> get() = _languageList

    fun fetchLanguages() {
        viewModelScope.launch {
            Log.d(null,"in fetchLang")
            val isMainThread = (Looper.getMainLooper() == Looper.myLooper())
            if(isMainThread){
                Log.d(null,"Yes its main thread")
            }else{
                Log.d(null,"Not main thread")
            }
            val context = getApplication<Application>().applicationContext
            repository.fetchLanguagesFromApi(context)

            val languages = repository.getAllLanguages()
            _languageList.postValue(languages) // value vs postValue
        }
    }
}
