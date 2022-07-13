package com.dsfgland.goa.presentation.game

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dsfgland.goa.R
import com.dsfgland.goa.databinding.FragmentLepGameBinding
import com.dsfgland.goa.util.Winner
import com.dsfgland.goa.util.anim.show

class LepGameFragment : Fragment(R.layout.fragment_lep_game) {
    private var _binding: FragmentLepGameBinding? = null
    private val binding: FragmentLepGameBinding get() = _binding!!
    private var winner = Winner.None

    @DrawableRes
    private var humanSide = -1

    @DrawableRes
    private var computerSide = -1

    private var computerMoves = 0
    private var humanMoves = 0

    private lateinit var allCells: List<ImageView>
    private val availableCells = mutableListOf<ImageView>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLepGameBinding.bind(view)
        humanSide = requireArguments().getInt("side")
        computerSide = if (humanSide == R.drawable.hat) R.drawable.beer else R.drawable.hat
        initCells()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initCells() {
        allCells = listOf(
            binding.iv11,
            binding.iv12,
            binding.iv13,
            binding.iv21,
            binding.iv22,
            binding.iv23,
            binding.iv31,
            binding.iv32,
            binding.iv33,
        )
        allCells.forEach {
            it.isEnabled = false
            it.tag = -1
            it.setOnClickListener { clicked ->
                humanMove(clicked as ImageView)
            }
        }
        availableCells.addAll(allCells)
        computerMove()
    }

    private fun computerMove() {
        with(availableCells.shuffled().random()) {
            tag = computerSide
            setImageResource(computerSide)
            availableCells.remove(this)
            computerMoves++
            show(this) {
                if ((computerMoves > 2 || humanMoves > 2) && determineWinner() != -1) {
                    winner =
                        if (determineWinner() == computerSide) Winner.Computer else Winner.Human
                    findNavController().navigate(
                        R.id.action_lepGameFragment_to_lepGameResultsFragment,
                        bundleOf("winner" to winner)
                    )
                } else {
                    if (availableCells.isEmpty()) findNavController().navigate(
                        R.id.action_lepGameFragment_to_lepGameResultsFragment,
                        bundleOf("winner" to winner)
                    )
                    availableCells.forEach {
                        it.isEnabled = true
                    }
                }
            }
        }
    }

    private fun humanMove(view: ImageView) {
        availableCells.forEach { it.isEnabled = false }
        with(view) {
            tag = humanSide
            setImageResource(humanSide)
            availableCells.remove(this)
            humanMoves++
            show(this) {
                if ((computerMoves > 2 || humanMoves > 2) && determineWinner() != -1) {
                    winner =
                        if (determineWinner() == computerSide) Winner.Computer else Winner.Human
                    findNavController().navigate(
                        R.id.action_lepGameFragment_to_lepGameResultsFragment,
                        bundleOf("winner" to winner)
                    )
                } else {
                    computerMove()
                }
            }
        }
    }

    private fun determineWinner(): Int {
        return when {
            //horizontal
            binding.iv11.tag == binding.iv12.tag && binding.iv12.tag == binding.iv13.tag -> binding.iv11.tag as Int
            binding.iv21.tag == binding.iv22.tag && binding.iv22.tag == binding.iv23.tag -> binding.iv21.tag as Int
            binding.iv31.tag == binding.iv32.tag && binding.iv32.tag == binding.iv33.tag -> binding.iv31.tag as Int

            //vertical
            binding.iv11.tag == binding.iv21.tag && binding.iv21.tag == binding.iv31.tag -> binding.iv11.tag as Int
            binding.iv12.tag == binding.iv22.tag && binding.iv22.tag == binding.iv32.tag -> binding.iv12.tag as Int
            binding.iv13.tag == binding.iv23.tag && binding.iv23.tag == binding.iv33.tag -> binding.iv13.tag as Int

            //diagonal
            binding.iv11.tag == binding.iv22.tag && binding.iv22.tag == binding.iv33.tag -> binding.iv11.tag as Int
            binding.iv13.tag == binding.iv22.tag && binding.iv22.tag == binding.iv31.tag -> binding.iv13.tag as Int
            else -> -1
        }
    }
}