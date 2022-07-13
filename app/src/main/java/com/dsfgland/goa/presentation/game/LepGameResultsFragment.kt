package com.dsfgland.goa.presentation.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dsfgland.goa.R
import com.dsfgland.goa.databinding.FragmentLepGameResultBinding
import com.dsfgland.goa.util.Winner

class LepGameResultsFragment : Fragment(R.layout.fragment_lep_game_result) {
    private var _binding: FragmentLepGameResultBinding? = null
    private val binding: FragmentLepGameResultBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLepGameResultBinding.bind(view)

        binding.winnerLabel.text = when (requireArguments().get("winner") as Winner) {
            Winner.Human -> "You won"
            Winner.Computer -> "You lose"
            Winner.None -> "Draw"
        }
        binding.buttonTryAgain.setOnClickListener {
            findNavController().popBackStack(R.id.lepGameSettingsFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}