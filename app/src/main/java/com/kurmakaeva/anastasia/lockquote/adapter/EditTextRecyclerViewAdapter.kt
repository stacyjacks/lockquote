package com.kurmakaeva.anastasia.lockquote.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kurmakaeva.anastasia.lockquote.R

// *** GAME TASK ONE FRAGMENT ADAPTER ***


interface RecyclerViewEditTextListener {
    fun onCorrectTextInputCallback()
}

class EditTextRecyclerViewAdapter(private val editTextInterface: RecyclerViewEditTextListener, private var modPassword: String) :
    RecyclerView.Adapter<EditTextViewHolder>() {

    private var editTextValues: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditTextViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_edittext_viewholder, parent, false)
        return EditTextViewHolder(view)
    }

    override fun getItemCount(): Int {
        val itemCount = modPassword.toCharArray().size
        if (editTextValues.count() != itemCount) {
            editTextValues = " ".repeat(itemCount)
        }
        return itemCount
    }

    override fun onBindViewHolder(holder: EditTextViewHolder, position: Int) {
        holder.letterBox.hint = modPassword[position].toString()
            holder.textWatcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (holder.letterBox.text.toString() == modPassword[position].toString()) {
                    editTextValues = editTextValues.replaceRange(position, position + 1, s)
                }

                if (s.isBlank()) {
                    holder.letterBox.background = ContextCompat.getDrawable(holder.letterBox.context, R.drawable.edit_text_style)
                    holder.letterBox.focusSearch(View.FOCUS_LEFT)?.requestFocus()
//                        holder.letterBox.setOnKeyListener { v, keyCode, event ->
//                            if (keyCode == KeyEvent.KEYCODE_DEL) {
//                                holder.letterBox.focusSearch(View.FOCUS_LEFT)?.requestFocus()
//                            }
//                            false
//                        }
                } else {
                    holder.letterBox.focusSearch(View.FOCUS_RIGHT)?.requestFocus()
                }

                if (s.toString() == modPassword[position].toString()) {
                    holder.letterBox.background = ContextCompat.getDrawable(holder.letterBox.context, R.drawable.bubble)
                } else {
                    holder.letterBox.requestFocus()
                    holder.letterBox.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                    holder.letterBox.text?.clear()
                }

                if (editTextValues == modPassword) {
                    editTextInterface.onCorrectTextInputCallback()
                    holder.letterBox.clearFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {

            }
        }
        holder.letterBox.addTextChangedListener(holder.textWatcher)
    }
}