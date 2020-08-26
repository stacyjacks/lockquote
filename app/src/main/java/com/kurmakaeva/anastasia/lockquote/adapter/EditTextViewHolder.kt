package com.kurmakaeva.anastasia.lockquote.adapter

import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kurmakaeva.anastasia.lockquote.R
import com.google.android.material.textfield.TextInputEditText

class EditTextViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val letterBox = itemView.findViewById(R.id.editTextField) as TextInputEditText
    lateinit var textWatcher: TextWatcher
}