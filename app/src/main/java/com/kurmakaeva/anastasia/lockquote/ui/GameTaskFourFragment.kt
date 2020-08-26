package com.kurmakaeva.anastasia.lockquote.ui

import android.content.Context
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.kurmakaeva.anastasia.lockquote.R

class GameTaskFourFragment : Fragment(),
    OnDataPass {

    lateinit var dataPass: OnDataPass
    private lateinit var radioGroup: RadioGroup
    private lateinit var correctAnswerButton: RadioButton
    private lateinit var incorrectAnswerButton1: RadioButton
    private lateinit var incorrectAnswerButton3: RadioButton
    private lateinit var incorrectAnswerButton4: RadioButton

    companion object {
        fun newInstance(): GameTaskFourFragment {
            return GameTaskFourFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_task_four, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        radioGroup = view.findViewById(R.id.multipleChoiceTaskFour)
        correctAnswerButton = view.findViewById(R.id.radioButton2)
        incorrectAnswerButton1 = view.findViewById(R.id.radioButton1)
        incorrectAnswerButton3 = view.findViewById(R.id.radioButton3)
        incorrectAnswerButton4 = view.findViewById(R.id.radioButton4)

        correctAnswerButton.text = passwordString()
        val shuffledPasswordString = passwordString()
            .toCharArray()
            .toMutableList()
            .shuffled()
            .toString()
            .replace("[\\[\\]]", "")
            .replace(",", "")
            .replace("]", "")
            .replace("[", "")
            .replace(" ", "")
        incorrectAnswerButton1.text = shuffledPasswordString
        incorrectAnswerButton3.text = passwordString().reversed()
        incorrectAnswerButton4.text = shuffledPasswordString.reversed().toLowerCase()

        onRadioButtonClicked()
        onContinueTapped()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPass = context as OnDataPass
    }

    override fun onDataPass(data: String) {
        dataPass.onDataPass(data)
    }

    override fun passwordString(): String {
        return dataPass.passwordString()
    }

    override fun selectedLyric(): String {
        return dataPass.selectedLyric()

    }

    private fun onRadioButtonClicked() {
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when {
                correctAnswerButton.isChecked -> {
                    correctAnswerButton.background = ContextCompat.getDrawable(correctAnswerButton.context, R.drawable.bubble)

                    val blinkingAnimation: Animation = AlphaAnimation(0.0f, 1.0f)
                    blinkingAnimation.duration = 100
                    blinkingAnimation.startOffset = 20
                    blinkingAnimation.repeatMode = Animation.REVERSE
                    blinkingAnimation.repeatCount = 5
                    correctAnswerButton.startAnimation(blinkingAnimation)
                }
                incorrectAnswerButton1.isChecked -> {
                    incorrectAnswerButton1.background = ContextCompat.getDrawable(incorrectAnswerButton1.context,
                        R.drawable.bubble_red
                    )
                    incorrectAnswerButton1.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                }
                incorrectAnswerButton3.isChecked -> {
                    incorrectAnswerButton3.background = ContextCompat.getDrawable(incorrectAnswerButton3.context,
                        R.drawable.bubble_red
                    )
                    incorrectAnswerButton3.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                }
                incorrectAnswerButton4.isChecked -> {
                    incorrectAnswerButton4.background = ContextCompat.getDrawable(incorrectAnswerButton4.context,
                        R.drawable.bubble_red
                    )
                    incorrectAnswerButton4.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                }
            }
            if (correctAnswerButton.isChecked) {
                onCorrectChoice()
            }
        }
    }

    private fun onCorrectChoice() {
        val checkAnimation = view?.findViewById<LottieAnimationView>(R.id.checkViewAnimation)
        checkAnimation?.visibility = LottieAnimationView.VISIBLE
        val successTaskFour = view?.findViewById<LinearLayout>(R.id.successTaskFour)
        successTaskFour?.visibility = View.VISIBLE
    }

    private fun onContinueTapped() {
        val continueButton = view?.findViewById<Button>(R.id.continueButtonTaskFour)
        continueButton?.setOnClickListener {
            val fragmentTaskFive = GameTaskFiveFragment.newInstance()
            val transaction = fragmentManager?.beginTransaction()
            transaction
                ?.replace(R.id.frameFragmentGame, fragmentTaskFive)
                ?.addToBackStack(null)
                ?.commit()
        }
    }
}