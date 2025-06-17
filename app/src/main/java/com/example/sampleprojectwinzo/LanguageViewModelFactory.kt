package com.example.sampleprojectwinzo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LanguageViewModelFactory(application: Application) : ViewModelProvider.Factory {

    private val repository = Repository(application)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LanguageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LanguageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}