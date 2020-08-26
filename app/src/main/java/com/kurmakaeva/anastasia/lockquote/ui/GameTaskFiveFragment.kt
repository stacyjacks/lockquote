package com.kurmakaeva.anastasia.lockquote.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.kurmakaeva.anastasia.lockquote.R
import com.google.android.material.textfield.TextInputEditText


class GameTaskFiveFragment: Fragment(), OnDataPass {

    lateinit var dataPass: OnDataPass
    private lateinit var editTextFieldOne: TextInputEditText
    private lateinit var editTextFieldTwo: TextInputEditText
    private lateinit var editTextFieldThree: TextInputEditText
    private lateinit var editTextFieldFour: TextInputEditText
    private lateinit var editTextFieldFive: TextInputEditText
    private lateinit var editTextFieldSix: TextInputEditText
    private lateinit var editTextFieldSeven: TextInputEditText
    private lateinit var editTextFieldEight: TextInputEditText
    private lateinit var editTextFieldNine: TextInputEditText
    private lateinit var editTextFieldTen: TextInputEditText
    private lateinit var editTextFieldEleven: TextInputEditText
    private lateinit var editTextFieldTwelve: TextInputEditText
    private lateinit var editTextFieldThirteen: TextInputEditText
    private lateinit var editTextFieldFourteen: TextInputEditText
    private lateinit var editTextFieldFifteen: TextInputEditText

    companion object {
        fun newInstance(): GameTaskFiveFragment {
            return GameTaskFiveFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_task_five, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextFieldOne = view.findViewById(R.id.editTextField1)
        editTextFieldTwo = view.findViewById(R.id.editTextField2)
        editTextFieldThree = view.findViewById(R.id.editTextField3)
        editTextFieldFour = view.findViewById(R.id.editTextField4)
        editTextFieldFive = view.findViewById(R.id.editTextField5)
        editTextFieldSix = view.findViewById(R.id.editTextField6)
        editTextFieldSeven = view.findViewById(R.id.editTextField7)
        editTextFieldEight = view.findViewById(R.id.editTextField8)
        editTextFieldNine = view.findViewById(R.id.editTextField9)
        editTextFieldTen = view.findViewById(R.id.editTextField10)
        editTextFieldEleven = view.findViewById(R.id.editTextField11)
        editTextFieldTwelve = view.findViewById(R.id.editTextField12)
        editTextFieldThirteen = view.findViewById(R.id.editTextField13)
        editTextFieldFourteen = view.findViewById(R.id.editTextField14)
        editTextFieldFifteen = view.findViewById(R.id.editTextField15)

        val editTextFields: Array<TextInputEditText> = arrayOf(
            editTextFieldOne,
            editTextFieldTwo,
            editTextFieldThree,
            editTextFieldFour,
            editTextFieldFive,
            editTextFieldSix,
            editTextFieldSeven,
            editTextFieldEight,
            editTextFieldNine,
            editTextFieldTen,
            editTextFieldEleven,
            editTextFieldTwelve,
            editTextFieldThirteen,
            editTextFieldFourteen,
            editTextFieldFifteen
        )

        val passwordCharCount = passwordString().count()
        editTextDisplayFirst(editTextFields, passwordCharCount)

        for (editTextField in editTextFields) {
                editTextField.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        val arrayPosition = editTextFields.indexOf(editTextField)
                        onTextInput(editTextFields, arrayPosition)
                        if (s.isBlank()) {
                            editTextField.background = ContextCompat.getDrawable(editTextField.context,
                                R.drawable.edit_text_style
                            )
                        }
                        onCorrectTextInputCallback(editTextFields, editTextFields.map { it.text }.joinToString(""))
                    }

                    override fun afterTextChanged(s: Editable) {
                    }

                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    }
                })
        }
        onContinueTapped()
        onStartOverTapped()
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

    private fun editTextDisplayFirst(editTextFields: Array<TextInputEditText>, number: Int)  {
        if (number-1 > editTextFields.size) { return }

        for (i in 0 until number) {
            editTextFields[i].visibility = View.VISIBLE
        }
    }

    private fun onTextInput(editTextFields: Array<TextInputEditText>, arrayPosition: Int) {
        val passwordStringCharArray = passwordString().toCharArray()

        if (!passwordStringCharArray.indices.contains(arrayPosition)) return
        if (!editTextFields.indices.contains(arrayPosition)) return

        if (editTextFields[arrayPosition].text.toString() == (passwordStringCharArray[arrayPosition].toString())) {
            editTextFields[arrayPosition].background = ContextCompat.getDrawable(editTextFields[arrayPosition].context, R.drawable.bubble)

            editTextFields[arrayPosition].focusSearch(View.FOCUS_RIGHT)?.requestFocus()
        } else {
            editTextFields[arrayPosition].requestFocus()
            editTextFields[arrayPosition].performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
            editTextFields[arrayPosition].text?.clear()
        }
    }

    private fun onCorrectTextInputCallback(editTextFields: Array<TextInputEditText>, computedPassword: String) {
        if (passwordString() == computedPassword) {
            hideSoftKeyboard(this.requireActivity())
            for (editTextField in editTextFields) {
                editTextField.clearFocus()
                editTextField.setOnTouchListener { view, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_BUTTON_PRESS) {
                        editTextField.isEnabled = false
                    }
                    true
                }
            }

            val helpButtons = view?.findViewById<LinearLayout>(R.id.helpButtons)
            helpButtons?.visibility = View.GONE

            val checkAnimation = view?.findViewById<LottieAnimationView>(R.id.checkViewAnimation)
            checkAnimation?.visibility = LottieAnimationView.VISIBLE
            val successTaskFive = view?.findViewById<LinearLayout>(R.id.successTaskFive)
            successTaskFive?.visibility = View.VISIBLE
        }
    }

    private fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus?.windowToken, 0
        )
    }

    private fun onContinueTapped() {
        val continueButton = view?.findViewById<Button>(R.id.continueButtonTaskFive)
        continueButton?.setOnClickListener {
            val fragmentGameResult = GameResultFragment.newInstance()
            val transaction = fragmentManager?.beginTransaction()
            transaction
                ?.replace(R.id.frameFragmentGame, fragmentGameResult)
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    private fun onStartOverTapped() {
        val startOverButton = view?.findViewById<Button>(R.id.takeMeBack)
        startOverButton?.setOnClickListener {
            activity?.finish()
        }
    }
}