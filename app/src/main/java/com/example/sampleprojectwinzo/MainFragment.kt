package com.example.sampleprojectwinzo

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val confirmButton: Button = view.findViewById(R.id.confirmButton)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager

        val factory = LanguageViewModelFactory(requireActivity().application)
        val viewModel: LanguageViewModel = ViewModelProvider(this, factory)[LanguageViewModel::class.java]

        viewModel.setlastClickedLanguageId()

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        Log.d(null, "before call")
        viewModel.fetchLanguages(requireContext().applicationContext)
        Log.d(null, "after call")

        viewModel.languageList.observe(viewLifecycleOwner) { languagesFromDb ->
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            val customAdapter = CustomAdapter(languagesFromDb, viewModel.getlastClickedLanguageId()) { selectedLanguage ->
                viewModel.setcurrentSelectedLanguage(selectedLanguage)

                if (selectedLanguage == null) {
                    confirmButton.text = getString(R.string.continuebuttontext)
                    confirmButton.setBackgroundColor(Color.LTGRAY)
                } else {
                    confirmButton.text = selectedLanguage.buttonText
                    confirmButton.setBackgroundColor("#2e9a05".toColorInt())
                    viewModel.writeToSP("clicked_language_id", selectedLanguage.languageId)
                }
            }
            recyclerView.adapter = customAdapter

            val restoredLanguage = languagesFromDb.find { it.languageId == viewModel.getlastClickedLanguageId() }
            viewModel.setcurrentSelectedLanguage(restoredLanguage)
            if (restoredLanguage != null) {
                confirmButton.text = restoredLanguage.buttonText
                confirmButton.setBackgroundColor("#2e9a05".toColorInt())
            } else {
                confirmButton.text = getString(R.string.continuebuttontext)
                confirmButton.setBackgroundColor(Color.LTGRAY)
            }
        }

        confirmButton.setOnClickListener {
            viewModel.getcurrentSelectedLanguage()?.let {
                viewModel.writeToSP("chosen_language_id", it.languageId)
                Toast.makeText(requireContext(), "Confirmed: ${it.languageName}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
