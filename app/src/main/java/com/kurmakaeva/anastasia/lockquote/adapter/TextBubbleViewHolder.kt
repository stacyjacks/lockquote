package com.kurmakaeva.anastasia.lockquote.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kurmakaeva.anastasia.lockquote.R

class TextBubbleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val textBubble = itemView.findViewById(R.id.textBubble) as TextView
}

interface OnTextBubbleStartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}