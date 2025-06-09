package com.example.sampleprojectwinzo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Language(
    @PrimaryKey
    var languageId: String,

    var languageImage: String,

    var languageName: String,

    var itemText: String,

    var buttonText: String
) : java.io.Serializable

