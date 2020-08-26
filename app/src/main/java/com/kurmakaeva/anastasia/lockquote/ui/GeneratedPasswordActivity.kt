package com.kurmakaeva.anastasia.lockquote.ui

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.text.SpannedString
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.airbnb.lottie.LottieAnimationView
import com.kurmakaeva.anastasia.lockquote.R
import kotlinx.android.synthetic.main.activity_generated_password.*

class GeneratedPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generated_password)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = (ContextCompat.getColor(this, R.color.colorAccentDark))

        handleIntent(intent)

        setTitle(R.string.generated_pass_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        val animation = findViewById<LottieAnimationView>(R.id.loadingPass)
        animation.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                val runningAnimation = findViewById<LottieAnimationView>(R.id.loadingPass)
                runningAnimation.visibility = View.GONE

                val justASecTV = findViewById<TextView>(R.id.justASec)
                justASecTV.visibility = View.GONE

                val mainLinearLayout = findViewById<LinearLayout>(R.id.main_content_linear_layout)
                mainLinearLayout.visibility = View.VISIBLE

                val bottomViewGroup = findViewById<LinearLayout>(R.id.bottom_view_group)
                bottomViewGroup.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }
        })

        val generatedPassTextView = findViewById<TextView>(R.id.generatedPass)
        val modPasswordString = charReplacer(passwordString())
        generatedPassTextView.text = modPasswordString

        val numberOfCharsTextView = findViewById<TextView>(R.id.numberOfCharacters)
        numberOfCharsTextView.text = numberOfCharCalculator(passwordString())

        val helpMeRemember = findViewById<Button>(R.id.helpMeRemember)
        helpMeRemember.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent
                .putExtra("modPasswordString", modPasswordString)
                .putExtra("selectedLyric", selectedText())
            startActivity(intent)
        }

        val tryAgain = findViewById<Button>(R.id.tryAgainButton)
        tryAgain.setOnClickListener {
            this.finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                this.onBackPressed()
            // Respond to the action bar's Up/Home button
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        selectedTextFromLyric.text = makeFirstLetterBold(selectedText())
    }

    private fun numberOfCharCalculator(passwordString: String): String {
        return passwordString.length.toString()
    }

    private fun makeFirstLetterBold(selectedLyric: String): SpannedString {
        var customString = SpannedString("")
        val regex = Regex("(\\s|\\\\n)")
        val lyricWordsArray = selectedLyric.split(regex).toTypedArray()
        for (word in lyricWordsArray) {
            if (word.isBlank()) {
                continue
            }

            val firstLetterBold = word.first()
            val normalText = word.drop(1)
            val customWord = SpannedString(buildSpannedString {
                bold {
                    append(firstLetterBold)
                }
                append(normalText)
            })
            customString = TextUtils.concat(customString, " ", customWord) as SpannedString
        }
        return customString.drop(1) as SpannedString
    }

    private fun selectedText(): String {
        return intent.getStringExtra("selectedText")
    }

    private fun passwordString(): String {
        return intent.getStringExtra("passwordString")
    }

    private fun charReplacer(passwordString: String): String {
        val replaceA = arrayListOf("A", "a", "4")
        val replaceS = arrayListOf("S", "s", "5")
        val replaceE = arrayListOf("E", "e", "3")
        val replaceT = arrayListOf("T", "t", "7")
        val replaceI = arrayListOf("I", "i", "1")
        val replaceO = arrayListOf("O", "o", "0")
        val replaceG = arrayListOf("G", "g", "6")

        return passwordString
            .replace("A", replaceA.random())
            .replace("Á", replaceA.random())
            .replace("À", replaceA.random())
            .replace("Â", replaceA.random())
            .replace("Ä", replaceA.random())
            .replace("á", replaceA.random())
            .replace("à", replaceA.random())
            .replace("â", replaceA.random())
            .replace("ä", replaceA.random())
            .replace("a", replaceA.random())
            .replace("S", replaceS.random())
            .replace("s", replaceS.random())
            .replace("E", replaceE.random())
            .replace("É", replaceE.random())
            .replace("È", replaceE.random())
            .replace("Ê", replaceE.random())
            .replace("Ë", replaceE.random())
            .replace("e", replaceE.random())
            .replace("é", replaceE.random())
            .replace("è", replaceE.random())
            .replace("ê", replaceE.random())
            .replace("ë", replaceE.random())
            .replace("T", replaceT.random())
            .replace("t", replaceT.random())
            .replace("I", replaceI.random())
            .replace("Í", replaceE.random())
            .replace("Ì", replaceE.random())
            .replace("Î", replaceE.random())
            .replace("Ï", replaceE.random())
            .replace("i", replaceI.random())
            .replace("í", replaceE.random())
            .replace("ì", replaceE.random())
            .replace("î", replaceE.random())
            .replace("ï", replaceE.random())
            .replace("O", replaceO.random())
            .replace("Ó", replaceE.random())
            .replace("Ò", replaceE.random())
            .replace("Ô", replaceE.random())
            .replace("Ö", replaceE.random())
            .replace("o", replaceO.random())
            .replace("ó", replaceE.random())
            .replace("ò", replaceE.random())
            .replace("ô", replaceE.random())
            .replace("ö", replaceE.random())
            .replace("G", replaceG.random())
            .replace("g", replaceG.random())
            .replace("Ú", "U")
            .replace("Ù", replaceE.random())
            .replace("Û", replaceE.random())
            .replace("Ü", replaceE.random())
            .replace("ú", "u")
            .replace("ù", replaceE.random())
            .replace("û", replaceE.random())
            .replace("ü", replaceE.random())
    }
}

