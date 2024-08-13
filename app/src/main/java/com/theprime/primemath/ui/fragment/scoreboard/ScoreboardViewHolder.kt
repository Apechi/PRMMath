package com.theprime.primemath.ui.fragment.scoreboard

import androidx.recyclerview.widget.RecyclerView
import com.theprime.primemath.R
import com.theprime.primemath.data.model.Score
import com.theprime.primemath.databinding.ScoreRowBinding

class ScoreboardViewHolder(
    private val binding: ScoreRowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(position: Int, score: Score) = with(binding) {
        nick.text = root.context.getString(R.string.scoreboard_format, position, score.nick)
        binding.score.text = score.score.toString()
    }
}