package com.theprime.primemath.ui.fragment.game.divisibility

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adsmedia.adsmodul.AdsHelper
import com.adsmedia.adsmodul.utils.AdsConfig
import com.theprime.primemath.R
import com.theprime.primemath.databinding.FragmentDivisibilityGameBinding
import com.theprime.primemath.util.base.BaseFragment
import org.koin.android.ext.android.inject
import java.lang.Exception


class DivisibilityGameFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_divisibility_game
    private var _binding: FragmentDivisibilityGameBinding? = null
    private val binding get() = _binding!!
    private val args: DivisibilityGameFragmentArgs by navArgs()
    private val viewModel: DivisibilityGameViewModel by inject()
    private var question = listOf<String>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        question = listOf(
            resources.getString(R.string.divisibility_question_p1),
            resources.getString(R.string.divisibility_question_p2),
            resources.getString(R.string.question_mark)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(
                DivisibilityGameFragmentDirections.actionDivisibilityGameFragmentToDivisibilityListFragment(
                    rate=0,
                    exerciseType = null,
                    player=args.player
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDivisibilityGameBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnswerButtons()
        binding.progressBar.max = DivisibilityGameViewModel.EXERCISES_COUNT
        binding.progressBar.progress = 0
        initViewModel()
    }

    private fun initViewModel() = with(binding){
        viewModel.getActiveEquation().observe(viewLifecycleOwner, Observer {
            resetColors()
            equationText.text = it.let {
                question[0]
                    .plus(" ")
                    .plus(it.componentA.toString())
                    .plus(" ")
                    .plus(question[1])
                    .plus(" ")
                    .plus(it.componentB.toString())
                    .plus(question[2])
            }
            equationNumbers.text = it.let {
                it.componentA.toString()
                    .plus(" ÷ ")
                    .plus(it.componentB.toString())
            }
        })

        viewModel.getEndGameEvent().observe(viewLifecycleOwner, Observer { rate ->
            AdsHelper.showInterstitial(requireActivity(), AdsConfig.Interstitial_ID, AdsConfig.Interval)
            findNavController().navigate(
                DivisibilityGameFragmentDirections.actionDivisibilityGameFragmentToDivisibilityListFragment(
                    rate=rate,
                    exerciseType=args.exerciseType,
                    player=args.player
                )
            )
        })
        viewModel.getAnswerEvent().observe(viewLifecycleOwner, Observer {
            when (it) {
                DivisibilityGameViewModel.AnswerEvent.VALID -> {
                    updateUiOnCorrectAnswer()
                    Handler(Looper.getMainLooper()).postDelayed({
                        if(isVisible) {
                            progressBar.progress = progressBar.progress + 1
                            viewModel.setNextActiveEquation()
                        }
                    }, 800)
                }
                DivisibilityGameViewModel.AnswerEvent.INVALID -> {
                    updateUiOnErrorAnswer()
                    Handler(Looper.getMainLooper()).postDelayed({
                        if(isVisible) {
                            viewModel.markActiveEquationAsFailed()
                            viewModel.setNextActiveEquation()
                        }
                    }, 2000)
                }
                null -> throw Exception("Error: AnswerEvent is null")
            }
        })
        viewModel.init(args)
    }

    private fun initAnswerButtons() = with(binding){
        yesButton.setOnClickListener {
            try {
                val userAnswer = 1
                viewModel.validateAnswer(userAnswer)
            } catch (exception: Exception) { }
        }
        noButton.setOnClickListener {
            try {
                val userAnswer = 0
                viewModel.validateAnswer(userAnswer)
            } catch (exception: Exception) { }
        }
    }


    private fun updateUiOnErrorAnswer() = with(binding){
        equationText.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        equationNumbers.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
    }

    private fun updateUiOnCorrectAnswer() = with(binding){
        equationText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        equationNumbers.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
    }

    private fun resetColors() = with(binding){
        equationText.setTextColor(ContextCompat.getColor(requireContext(), R.color.prime_blue))
        equationNumbers.setTextColor(ContextCompat.getColor(requireContext(), R.color.prime_blue))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}