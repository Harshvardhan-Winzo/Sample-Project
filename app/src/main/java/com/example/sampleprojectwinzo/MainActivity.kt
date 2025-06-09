package com.example.sampleprojectwinzo

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: LanguageViewModel
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var confirmButton: Button
    private var lastClickedLanguageId: String? = null
    private var currentSelectedLanguage: Language? = null

    companion object {
        private const val PREFS_NAME = "LanguagePrefs"
        private const val CLICKED_LANGUAGE_ID = "clicked_language_id"
        private const val CHOSEN_LANGUAGE_ID = "chosen_language_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    fun initialize() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        confirmButton = findViewById(R.id.confirmButton)

        sharedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        lastClickedLanguageId = sharedPrefs.getString(CLICKED_LANGUAGE_ID, null)

        val db = Room.databaseBuilder(
            applicationContext,
            LanguageDatabase::class.java,
            "database-name"
        ).build()

        val languageDao = db.LanguageDao()
        val repository = Repository(languageDao)

        val factory = LanguageViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, factory)[LanguageViewModel::class.java]

        Log.d(null,"before call")
        viewModel.fetchLanguages()
        Log.d(null,"after call")

        viewModel.languageList.observe(this) { languagesFromDb ->
            val customAdapter = CustomAdapter(languagesFromDb, lastClickedLanguageId) { selectedLanguage ->
                currentSelectedLanguage = selectedLanguage

                if (selectedLanguage == null) {
                    confirmButton.text = "CONTINUE"
                    confirmButton.setBackgroundColor(Color.LTGRAY)
                } else {
                    confirmButton.text = selectedLanguage.buttonText
                    confirmButton.setBackgroundColor(Color.parseColor("#2e9a05"))
                    sharedPrefs.edit { putString(CLICKED_LANGUAGE_ID, selectedLanguage.languageId) }
                }
            }
            recyclerView.adapter = customAdapter

            val restoredLanguage = languagesFromDb.find { it.languageId == lastClickedLanguageId }
            currentSelectedLanguage = restoredLanguage
            if (restoredLanguage != null) {
                confirmButton.text = restoredLanguage.buttonText
                confirmButton.setBackgroundColor(Color.parseColor("#2e9a05"))
            } else {
                confirmButton.text = "CONTINUE"
                confirmButton.setBackgroundColor(Color.LTGRAY)
            }

        }

        confirmButton.setOnClickListener {
            currentSelectedLanguage?.let {
                sharedPrefs.edit { putString(CHOSEN_LANGUAGE_ID, it.languageId) }
                Toast.makeText(this, "Confirmed: ${it.languageName}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}




