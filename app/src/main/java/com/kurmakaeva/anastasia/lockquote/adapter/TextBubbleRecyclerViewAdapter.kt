package com.kurmakaeva.anastasia.lockquote.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kurmakaeva.anastasia.lockquote.R
import java.util.*

// *** GAME TASK TWO FRAGMENT ADAPTER ***

interface RecyclerViewTextBubbleListener {
    fun onCorrectOrderCallback()
}

class TextBubbleRecyclerViewAdapter(private var lyricWordsArray: Array<String>,
                                    private val startDragListener: OnTextBubbleStartDragListener,
                                    private val textBubbleInterface: RecyclerViewTextBubbleListener): RecyclerView.Adapter<TextBubbleViewHolder>(), ItemMoveTextBubbleCallbackListener.Listener {

    private  var shuffledBubbles =  lyricWordsArray.toMutableList().shuffled()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextBubbleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_textbubble_viewholder, parent, false)
        return TextBubbleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shuffledBubbles.size
    }

    override fun onBindViewHolder(holder: TextBubbleViewHolder, position: Int) {
        holder.textBubble.text = shuffledBubbles[position]
        holder.itemView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                this.startDragListener.onStartDrag(holder)
            }
            return@setOnTouchListener true
        }

        if (shuffledBubbles.toTypedArray().contentEquals(lyricWordsArray)) {
            holder.textBubble.background = ContextCompat.getDrawable(holder.textBubble.context, R.drawable.bubble)
            textBubbleInterface.onCorrectOrderCallback()
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

        if (shuffledBubbles.toTypedArray().contentEquals(lyricWordsArray)) {
            this.notifyDataSetChanged()
        }

        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(itemViewHolder: TextBubbleViewHolder) {
    }

    override fun onRowClear(itemViewHolder: TextBubbleViewHolder) {
    }
}