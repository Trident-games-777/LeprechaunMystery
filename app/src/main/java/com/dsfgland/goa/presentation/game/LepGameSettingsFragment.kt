package com.dsfgland.goa.presentation.game

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dsfgland.goa.R
import com.dsfgland.goa.databinding.FragmentLepGameSettingsBinding

class LepGameSettingsFragment : Fragment(R.layout.fragment_lep_game_settings) {
    private var _binding: FragmentLepGameSettingsBinding? = null
    private val binding: FragmentLepGameSettingsBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLepGameSettingsBinding.bind(view)
        var humanSide: Int = -1

        binding.image1.setOnClickListener {
            binding.image2.visibility = View.GONE
            binding.buttonStart.isEnabled = true
            humanSide = R.drawable.hat
        }

        binding.image2.setOnClickListener {
            binding.image1.visibility = View.GONE
            binding.buttonStart.isEnabled = true
            humanSide = R.drawable.beer
        }

        binding.buttonStart.setOnClickListener {
            findNavController().navigate(
                R.id.action_lepGameSettingsFragment_to_lepGameFragment,
                bundleOf("side" to humanSide)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}