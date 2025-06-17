package com.example.sampleprojectwinzo


import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CustomAdapter(
    private val dataSet: List<Language>,
    private val previouslySelectedId: String?,
    private val onSelectionChanged: (Language?) -> Unit
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private var selectedPosition: Int = dataSet.indexOfFirst {it.languageId == previouslySelectedId}

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.languageicon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_display, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val language = dataSet[position]

        Glide.with(holder.imageView.context)
            .load(language.languageImage)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.imageView)

        if (position == selectedPosition) {
            holder.imageView.clearColorFilter()
        } else {
            val matrix = ColorMatrix()
            matrix.setSaturation(0f)
            holder.imageView.colorFilter = ColorMatrixColorFilter(matrix)
        }

        holder.imageView.setOnClickListener {
            val currentPos = holder.adapterPosition
            if (currentPos == RecyclerView.NO_POSITION) return@setOnClickListener

            if (currentPos == selectedPosition) {
                val previousSelected = selectedPosition
                selectedPosition = -1
                notifyItemChanged(previousSelected)
                onSelectionChanged(null)
            } else {
                val previousSelected = selectedPosition
                selectedPosition = currentPos
                if (previousSelected != -1) notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
                onSelectionChanged(dataSet[selectedPosition])
            }
        }
    }
}


