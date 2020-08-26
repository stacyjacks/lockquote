package com.kurmakaeva.anastasia.lockquote.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.kurmakaeva.anastasia.lockquote.R
import com.kurmakaeva.anastasia.lockquote.adapter.*
import com.google.android.flexbox.FlexboxLayoutManager

class GameTaskTwoFragment : Fragment(),
    OnDataPass, OnTextBubbleStartDragListener, RecyclerViewTextBubbleListener {
    lateinit var dataPass: OnDataPass
    lateinit var textBubbleRecyclerView: RecyclerView
    lateinit var adapter: TextBubbleRecyclerViewAdapter
    lateinit var touchHelper: ItemTouchHelper

    companion object {
        fun newInstance(): GameTaskTwoFragment {
            return GameTaskTwoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_task_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textBubbleRecyclerView = view.findViewById(R.id.textBubbleRV)
        textBubbleRecyclerView.layoutManager = FlexboxLayoutManager(context)
        adapter = TextBubbleRecyclerViewAdapter(divideLyricIntoWordsArray(), this, this)
        textBubbleRecyclerView.adapter = adapter

        val callback: ItemTouchHelper.Callback = ItemMoveTextBubbleCallbackListener(adapter)

        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(textBubbleRecyclerView)

        onContinueTapped()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPass = context as OnDataPass
    }

    override fun onDataPass(data: String) {
    }

    override fun passwordString(): String {
        return dataPass.passwordString()
    }

    override fun selectedLyric(): String {
        return dataPass.selectedLyric()
    }

    fun passData(data: String) {
        dataPass.onDataPass(data)
    }

    private fun divideLyricIntoWordsArray(): Array<String> {
        val selectedLyric = selectedLyric()
        val regex = Regex("(\\s|\\\\n)")
        val lyricWordsArray = selectedLyric.split(regex).toTypedArray()

        return lyricWordsArray.map { it.replace(",", "") }.toTypedArray()
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }

    override fun onCorrectOrderCallback() {
        val successTaskTwo = view?.findViewById<LinearLayout>(R.id.successTaskTwo)
        successTaskTwo?.visibility = View.VISIBLE
        textBubbleRecyclerView.addOnItemTouchListener(object: RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return true
            }
    })
    }

    private fun onContinueTapped() {
        val continueButton = view?.findViewById<Button>(R.id.continueButtonTaskTwo)
        continueButton?.setOnClickListener {
            val fragmentTaskThree =
                GameTaskThreeFragment.newInstance()
            val transaction = fragmentManager?.beginTransaction()
            transaction
                ?.replace(R.id.frameFragmentGame, fragmentTaskThree)
                ?.addToBackStack(null)
                ?.commit()
        }
    }
}
