package com.theprime.primemath.ui.fragment.unitsList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theprime.primemath.data.model.ExerciseType
import com.theprime.primemath.databinding.UnitsExerciseItemBinding

/**
 * Adapter that contains units exercise list.
 *
 */
class UnitsListAdapter(
    private val exercises: ArrayList<ExerciseType>,
    private val clickCallback: (exerciseType: ExerciseType) -> Unit
) : RecyclerView.Adapter<UnitsViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitsViewHolder {
        val binding = UnitsExerciseItemBinding.inflate(LayoutInflater.from(parent.context))
        return UnitsViewHolder(binding, clickCallback)
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: UnitsViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

}