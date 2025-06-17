package com.example.sampleprojectwinzo
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LanguageData(var languageData: ArrayList<Language>): Parcelable


