package com.theprime.primemath.ui.fragment.game.normal

import android.annotation.SuppressLint
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
import com.theprime.primemath.data.model.Equation
import com.theprime.primemath.data.model.Operation
import com.theprime.primemath.databinding.FragmentNormalGameBinding
import com.theprime.primemath.ui.view.KeyboardView
import com.theprime.primemath.util.base.BaseFragment
import com.theprime.primemath.util.gone
import com.theprime.primemath.util.visible
import org.koin.android.ext.android.inject
import java.lang.Exception

class NormalGameFragment : BaseFragment() {

    companion object {
        private const val DEFAULT_INPUT_VALUE = "?"
    }

    override val layout: Int = R.layout.fragment_normal_game

    private var _binding: FragmentNormalGameBinding? = null
    private val binding get() = _binding!!
    private val args: NormalGameFragmentArgs by navArgs()


    private val viewModel: NormalGameViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            when (args.exerciseType.operation){
                Operation.PLUS, Operation.MINUS, Operation.PLUS_MINUS ->
                    findNavController().navigate(
                        NormalGameFragmentDirections.actionNormalGameFragmentToAddSubListFragment(
                            rate=0,
                            exerciseType = null,
                            player=args.player
                        )
                    )
                Operation.MULTIPLY, Operation.DIVIDE, Operation.MULTIPLY_DIVIDE ->
                    findNavController().navigate(
                        NormalGameFragmentDirections.actionNormalGameFragmentToMulDivListFragment(
                            rate=0,
                            exerciseType = null,
                            player=args.player
                        )
                    )
                else -> throw Exception("Navigating to invalid operation!")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNormalGameBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initKeyboardListener()
        binding.progressBar.max = NormalGameViewModel.EXERCISES_COUNT
        binding.progressBar.progress = 0
        initViewModel()
    }

    private fun initViewModel() = with(binding){
        viewModel.getActiveEquation().observe(viewLifecycleOwner, Observer {
            resetColors()
            equation.text = it.let {
                it.componentA.toString()
                    .plus(" ")
                    .plus(
                        when (it.operation) {
                            Operation.PLUS -> "+"
                            Operation.MINUS -> "-"
                            Operation.MULTIPLY -> "×"
                            Operation.DIVIDE -> "÷"
                            else -> throw Exception("Invalid operation!")
                        }
                    )
                    .plus(" ")
                    .plus(it.componentB)
                    .plus(" = ")
            }
            input.text = DEFAULT_INPUT_VALUE
            if (args.exerciseType.difficulty <= 10 && args.exerciseType.operation != Operation.MULTIPLY && args.exerciseType.operation != Operation.DIVIDE && args.exerciseType.operation != Operation.MULTIPLY_DIVIDE) {
                graphicRepresentationContainer.visible()
                drawGraphicRepresentation(it)
            } else {
                graphicRepresentationContainer.gone()
            }
        })

        viewModel.getEndGameEvent().observe(viewLifecycleOwner, Observer { rate ->
            AdsHelper.showInterstitialPrime(requireActivity(), AdsConfig.Interstitial_ID, AdsConfig.Interval)
            when (args.exerciseType.operation){
                Operation.PLUS, Operation.MINUS, Operation.PLUS_MINUS ->
                    findNavController().navigate(
                        NormalGameFragmentDirections.actionNormalGameFragmentToAddSubListFragment(
                            rate=rate,
                            exerciseType=args.exerciseType,
                            player=args.player
                        )
                    )
                Operation.MULTIPLY, Operation.DIVIDE, Operation.MULTIPLY_DIVIDE ->
                    findNavController().navigate(
                        NormalGameFragmentDirections.actionNormalGameFragmentToMulDivListFragment(
                            rate=rate,
                            exerciseType=args.exerciseType,
                            player=args.player
                        )
                    )
                else -> throw Exception("Navigating to invalid operation!")
            }

        })
        viewModel.getAnswerEvent().observe(viewLifecycleOwner, Observer {
            when (it) {
                NormalGameViewModel.AnswerEvent.VALID -> {
                    updateUiOnCorrectAnswer()
                    Handler(Looper.getMainLooper()).postDelayed({
                        if(isVisible) {
                            progressBar.progress = progressBar.progress + 1
                            viewModel.setNextActiveEquation()
                        }
                    }, 1000)
                }
                NormalGameViewModel.AnswerEvent.INVALID -> {
                    updateUiOnErrorAnswer()
                    viewModel.markActiveEquationAsFailed()
                }
                null -> throw Exception("Error: AnswerEvent is null")
            }
        })
        viewModel.init(args)
    }

    private fun initKeyboardListener() = with(binding) {
        keyboardView.setListener(object : KeyboardView.InputListener {
            @SuppressLint("SetTextI18n")
            override fun onNumberClicked(value: Int) {
                if (input.text.length > 2) {
                    return
                }
                if (input.text.toString() == DEFAULT_INPUT_VALUE) {
                    input.text = ""
                }
                input.text = input.text.toString() + value
                resetColors()
            }

            override fun onBackspaceClicked() {
                resetColors()
                if (input.text.length <= 1) {
                    input.text = DEFAULT_INPUT_VALUE
                } else {
                    input.text =
                        input.text.toString().substring(0, input.text.toString().length - 1)
                }
            }

            override fun onAcceptClicked() {
                try {
                    val userAnswer = input.text.toString().toInt()
                    viewModel.validateAnswer(userAnswer)
                } catch (exception: Exception) {
                    return
                }
            }

        })
    }


    private fun updateUiOnErrorAnswer() = with(binding){
        input.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        equation.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
    }

    private fun updateUiOnCorrectAnswer() = with(binding) {
        input.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        equation.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
    }

    private fun resetColors() = with(binding) {
        input.setTextColor(ContextCompat.getColor(requireContext(), R.color.prime_blue))
        equation.setTextColor(ContextCompat.getColor(requireContext(), R.color.prime_blue))
    }

    private fun drawGraphicRepresentation(activeEquation: Equation) = with(binding) {
        val shapes =
            arrayOf(shape1, shape2, shape3, shape4, shape5, shape6, shape7, shape8, shape9, shape10)
        when (activeEquation.operation) {
            Operation.PLUS -> {
                val sum = activeEquation.componentA + activeEquation.componentB
                for (i in 0 until activeEquation.componentA) {
                    shapes[i].visible()
                    shapes[i].setImageResource(R.drawable.square)
                }
                for (i in activeEquation.componentA until sum) {
                    println("$activeEquation, $i, $sum")
                    shapes[i].visible()
                    shapes[i].setImageResource(R.drawable.circle_fill)
                }
                for (i in sum until shapes.size) {
                    shapes[i].gone()
                }
            }
            Operation.MINUS -> {
                for (i in 0 until activeEquation.correctAnswer) {
                    shapes[i].visible()
                    shapes[i].setImageResource(R.drawable.circle_fill)
                }
                for (i in activeEquation.correctAnswer until activeEquation.correctAnswer + activeEquation.componentB) {
                    shapes[i].visible()
                    shapes[i].setImageResource(R.drawable.circle_empty)
                }
                for (i in activeEquation.correctAnswer + activeEquation.componentB until shapes.size) {
                    shapes[i].gone()
                }
            }
            else -> return
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
