package com.kurmakaeva.anastasia.lockquote.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kurmakaeva.anastasia.lockquote.R

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = (ContextCompat.getColor(this, R.color.colorAccentDark))

        val constraintLayout = findViewById<ConstraintLayout>(R.id.activity_main_layout)
        val animationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        searchViewSetUp()
    }

    private fun searchViewSetUp() {
        val searchView: SearchView = findViewById(R.id.search_view_main)
        searchView.queryHint = getString(R.string.query_hint)
        searchView.fitsSystemWindows = true
        searchView.onActionViewExpanded()

        val backgroundView = searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
        backgroundView.background = null

        Handler().postDelayed({ searchView.clearFocus() }, 0)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                intent.putExtra("query", query)
                intent.action = Intent.ACTION_SEARCH
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
}