package com.kurmakaeva.anastasia.lockquote.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kurmakaeva.anastasia.lockquote.R
import java.util.*

// *** GAME TASK THREE FRAGMENT ADAPTER ***

interface RecyclerViewCharBubbleListener {
    fun onCorrectOrderCallback()
}

class CharBubbleRecyclerViewAdapter(private var passwordCharArray: Array<String>,
                                    private val charBubbleStartDragListener: OnCharBubbleStartDragListener,
                                    private val charBubbleInterface: RecyclerViewCharBubbleListener) : RecyclerView.Adapter<CharBubbleViewHolder>(), ItemMoveCharBubbleCallbackListener.Listener {

    private val modPasswordCharArray: Array<String> = passwordCharArray.drop(1).dropLast(1).toTypedArray()
    private var shuffledBubbles = modPasswordCharArray.toMutableList().shuffled()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharBubbleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_charbubble_viewholder, parent, false)
        return CharBubbleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shuffledBubbles.size
    }

    override fun onBindViewHolder(holder: CharBubbleViewHolder, position: Int) {
        holder.charBubble.text = shuffledBubbles[position]
        holder.itemView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                this.charBubbleStartDragListener.onStartDrag(holder)
            }
            return@setOnTouchListener true
        }

        if (shuffledBubbles.toTypedArray().contentEquals(modPasswordCharArray)) {
            holder.charBubble.background =
                ContextCompat.getDrawable(holder.charBubble.context, R.drawable.bubble)
            charBubbleInterface.onCorrectOrderCallback()
        }
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(shuffledBubbles, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(shuffledBubbles, i, i - 1)
            }
        }

        if (shuffledBubbles.toTypedArray().contentEquals(modPasswordCharArray)) {
            this.notifyDataSetChanged()
        }

        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(itemViewHolder: CharBubbleViewHolder) {
    }

    override fun onRowClear(itemViewHolder: CharBubbleViewHolder) {
    }
}