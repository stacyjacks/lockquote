package com.kurmakaeva.anastasia.lockquote.ui

import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings.MENU_ITEM_WEB_SEARCH
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kurmakaeva.anastasia.lockquote.R
import kotlinx.android.synthetic.main.fragment_lyric_webview.*

class LyricWebViewFragment : Fragment() {
    var lyricUrl: String? = null

    companion object {
        fun newInstance(): LyricWebViewFragment {
            return LyricWebViewFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val webViewLayout = inflater.inflate(R.layout.fragment_lyric_webview, container, false)
        val lyricWebView: WebView = webViewLayout.findViewById(R.id.lyricWebView)
        val useSelectionButton = webViewLayout.findViewById<Button>(R.id.useSelectionButton)
        var numberOfWords = 0

        // Enable Javascript
        val webSettings = lyricWebView.settings

        // webSettings.javaScriptEnabled = true
        webSettings.allowContentAccess = true
        webSettings.disabledActionModeMenuItems = MENU_ITEM_WEB_SEARCH

        lyricWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return true
            }
        }

        val clipboard = activity?.getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.addPrimaryClipChangedListener {
            val regexWhiteSpace = Regex("(\\s+|\\\\n)")
            var selectedText = clipboard.primaryClip?.getItemAt(0)?.text

            if (selectedText != null) {
                val lyricSelectionTextView = view?.findViewById<TextView>(R.id.lyricSelectionTextView)
                selectedText = stripOfBracketContent(selectedText.toString())
                selectedText = removeWeirdChars(selectedText.toString()).trim()
                lyricSelectionTextView?.text = selectedText
                lyricSelectionTextView?.movementMethod = ScrollingMovementMethod()
            } else {
                showError(
                    requireContext(),
                    getString(R.string.error_something_wrong)
                )
            }

            numberOfWords = selectedText?.split(regexWhiteSpace)?.count() ?: 0

            useSelectionButton.text = String.format(context?.resources?.getString(R.string.use_button) + " ($numberOfWords)")

        }

        lyricUrl?.let { lyricWebView.loadUrl(it) }
            ?: context?.let {
                showError(
                    it,
                    getString(R.string.error_cant_open)
                )
            }

        useSelectionButton.text = String.format(resources.getString(R.string.use_button) + " ($numberOfWords)")

        useSelectionButton.setOnClickListener {
            extractSelection(numberOfWords)
        }


        return webViewLayout
    }

    private fun extractSelection(numberOfWords: Int) {
        val selectTextHint = getText(R.string.select_text_hint)
        val selectedText = removeWeirdChars(lyricSelectionTextView
            .text
            .toString()
        ).trim()

        when {
            numberOfWords > 15 -> {
                context?.let {
                    showError(
                        it,
                        getString(R.string.error_too_many_words)
                    )
                }
            }
            lyricSelectionTextView.text.contains(selectTextHint) -> {
                context?.let {
                    showError(
                        it,
                        getString(R.string.error_copy_not_tapped)
                    )
                }
            }
            else -> {
                val passwordString = firstCharOfEveryWordOf(selectedText).joinToString("")
                val intent = Intent(context, GeneratedPasswordActivity::class.java)
                intent
                    .putExtra("selectedText", selectedText)
                    .putExtra("passwordString", passwordString)
                startActivity(intent)
            }
        }
    }

    private fun stripOfBracketContent(value: String?): String? {
        val string = value ?: return value

        val startingBracket = string.indexOf("[")
        val closingBracket = string.indexOf("]")

        if (startingBracket == -1 || closingBracket == -1) { return value }

        return value.removeRange(startingBracket, closingBracket+1)
    }

    private fun removeWeirdChars(selectedText: String): String {
        val modified = selectedText.trim()

        return modified
            .replace("\n", " ")
            .replace("(", "")
            .replace(")", "")
            .replace("\"", "")
            .replace("'", "")
    }

    private fun firstCharOfEveryWordOf(selectedTextFromLyric: String): ArrayList<String> {
        val regex = Regex("(\\s+|\\\\n)")
        return ArrayList(
            selectedTextFromLyric
                .split(regex)
                .map {
                    if (it.isEmpty()) { return@map "" }
                    it.first().toString()
                }
                .filter { !it.isBlank() }
                .joinToString(",")
                .filter { it.isLetterOrDigit() }
                .split(",")
        )
    }
}