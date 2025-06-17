package com.example.sampleprojectwinzo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class Language(
    @PrimaryKey
    var languageId: String,

    var languageImage: String,

    var languageName: String,

    var itemText: String,

    var buttonText: String
) : Parcelable

