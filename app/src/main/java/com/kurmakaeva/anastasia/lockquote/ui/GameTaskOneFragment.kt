package com.kurmakaeva.anastasia.lockquote.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kurmakaeva.anastasia.lockquote.R
import com.kurmakaeva.anastasia.lockquote.adapter.EditTextRecyclerViewAdapter
import com.kurmakaeva.anastasia.lockquote.adapter.RecyclerViewEditTextListener

interface OnDataPass {
    fun onDataPass(data: String)
    fun passwordString(): String
    fun selectedLyric(): String
}

class GameTaskOneFragment : Fragment(), RecyclerViewEditTextListener {
    lateinit var editTextRecyclerView: RecyclerView
    lateinit var adapter: EditTextRecyclerViewAdapter
    lateinit var dataPass: OnDataPass

    companion object {
        fun newInstance(): GameTaskOneFragment {
            return GameTaskOneFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_task_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskOneTextView = view.findViewById<TextView>(R.id.helpfulTextTaskOne)
        val taskOneHelpfulText = getString(R.string.taskOneInputFullPassword)

        taskOneTextView.text = taskOneHelpfulText
        editTextRecyclerView = view.findViewById(R.id.editTextRV)
        editTextRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        adapter = EditTextRecyclerViewAdapter(this, dataPass.passwordString())
        editTextRecyclerView.adapter = adapter

        val clearButton = view.findViewById<Button>(R.id.clearPassButton)
        clearButton.setOnClickListener {
            editTextRecyclerView.adapter = null
            editTextRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        onContinueTapped()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPass = context as OnDataPass
    }

    override fun onCorrectTextInputCallback() {
        hideSoftKeyboard(this.requireActivity())
        editTextRecyclerView.addOnItemTouchListener(object: RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return true
            }
        })
        val clearButton = view?.findViewById<Button>(R.id.clearPassButton)
        clearButton?.visibility = View.INVISIBLE
        val successMessage = view?.findViewById<LinearLayout>(R.id.successTaskOne)
        successMessage?.visibility = View.VISIBLE
    }

    private fun onContinueTapped() {
        val continueButton = view?.findViewById<Button>(R.id.continueButtonTaskOne)
        continueButton?.setOnClickListener {
            val fragmentTaskTwo = GameTaskTwoFragment.newInstance()
            val transaction = fragmentManager?.beginTransaction()
            transaction
                ?.replace(R.id.frameFragmentGame, fragmentTaskTwo)
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    private fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus?.windowToken, 0
        )
    }

    fun passData(data: String){
        dataPass.onDataPass(data)
    }
}