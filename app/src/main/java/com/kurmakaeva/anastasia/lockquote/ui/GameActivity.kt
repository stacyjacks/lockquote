package com.kurmakaeva.anastasia.lockquote.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kurmakaeva.anastasia.lockquote.R

class GameActivity : AppCompatActivity(),
    OnDataPass {

    companion object {
        const val TAG_GAME_TASK_ONE_FRAGMENT = "GameTaskOneFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = (ContextCompat.getColor(this, R.color.colorAccentDark))

        showGameTaskOneFragment()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.elevation = 0f
        setTitle(R.string.memorisation_game_activity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                super.onBackPressed()
            // Respond to the action bar's Up/Home button
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onDataPass(data: String) {
    }

    override fun passwordString(): String {
        return intent.getStringExtra("modPasswordString")
    }

    override fun selectedLyric(): String {
        return intent.getStringExtra("selectedLyric")
    }

    private fun createGameTaskOneFragment(): GameTaskOneFragment {
        var gameTaskOneFragment = supportFragmentManager.findFragmentByTag(
            TAG_GAME_TASK_ONE_FRAGMENT
        ) as GameTaskOneFragment?

        if (gameTaskOneFragment == null) {
            gameTaskOneFragment =
                GameTaskOneFragment.newInstance()
        }
        return gameTaskOneFragment
    }

    private fun showGameTaskOneFragment() {
        val gameTaskOneFragment = createGameTaskOneFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.frameFragmentGame, gameTaskOneFragment,
                TAG_GAME_TASK_ONE_FRAGMENT
            )
            .commit()
    }

    fun showError(context: Context, message: String) {
        AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton(context.getString(R.string.ok_button), null)
            .create()
            .show()
    }
}
