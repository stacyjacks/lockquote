package com.kurmakaeva.anastasia.lockquote.ui

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kurmakaeva.anastasia.lockquote.network.Manager
import com.kurmakaeva.anastasia.lockquote.network.NetworkResult
import com.kurmakaeva.anastasia.lockquote.R
import com.kurmakaeva.anastasia.lockquote.adapter.GeniusSongSearchAdapter
import com.kurmakaeva.anastasia.lockquote.repository.GeniusRepo
import com.kurmakaeva.anastasia.lockquote.service.GeniusSearchService
import com.kurmakaeva.anastasia.lockquote.viewmodel.SearchViewModel
import com.kurmakaeva.anastasia.lockquote.viewmodel.SearchViewModel.*
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), GeniusSongSearchAdapter.SongSearchAdapterListener {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var songSearchAdapter: GeniusSongSearchAdapter

    private val networkManager by lazy { Manager(this) }

    companion object {
        const val TAG_LYRIC_WEBVIEW_FRAGMENT = "LyricWebViewFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = (ContextCompat.getColor(this, R.color.colorAccentDark))

        setTitle(R.string.search_results)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        setupViewModels()
        updateControls()

        searchViewSetUp()

        networkManager.result.observe(this,
            Observer<NetworkResult> { result ->
                when(result) {
                    NetworkResult.DISCONNECTED -> {
                        with(internetStatus) {
                            internetStatus.visibility = View.VISIBLE
                            internetStatusLL.visibility = View.VISIBLE
                            setText(result.messageResId)
                            setTextColor(ContextCompat.getColor(this@SearchActivity, result.colorResId)
                            )
                        }
                    }
                    NetworkResult.CONNECTED -> {
                        internetStatus.visibility = View.GONE
                        internetStatusLL.visibility = View.GONE
                        handleIntent(intent)
                    }

                    else -> return@Observer
                }
            })

        addBackStackListener()
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

    override fun onStart() {
        super.onStart()
        networkManager.registerCallback()
    }

    override fun onStop() {
        super.onStop()
        networkManager.unregisterCallback()
    }

    override fun onShowDetails(songSummaryViewData: SongSummaryViewData) {
        val lyricUrl =
            "https://www.google.com/amp/s/genius.com/amp" + songSummaryViewData.path // https://www.google.com/amp/s/genius.com/amp/Arctic-monkeys-fluorescent-adolescent-lyrics
        showLyricWebViewFragment(lyricUrl)

    }

    private fun performSearch(term: String) {
        showLoadingProgress()
        searchViewModel.searchSongs(term) { results ->
            hideLoadingProgress()
            songSearchAdapter.setSearchData(results)
        }
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra("query")
            performSearch(query)
        }
    }

    private fun setupViewModels() {
        val searchService = GeniusSearchService.instance
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        searchViewModel.geniusRepo = GeniusRepo(searchService)
    }

    private fun updateControls() {
        searchResultRecyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        searchResultRecyclerView.layoutManager = layoutManager

        songSearchAdapter = GeniusSongSearchAdapter(null, this, this)
        searchResultRecyclerView.adapter = songSearchAdapter
    }

    private fun searchViewSetUp() {
        val searchView: SearchView = findViewById(R.id.search_view_search_results)
        searchView.queryHint = intent.getStringExtra("query")
        searchView.onActionViewExpanded()

        val backgroundView = searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
        backgroundView.background = null

        Handler().postDelayed({ searchView.clearFocus() }, 0)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(componentName)
        )

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                showLoadingProgress()
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchView.queryHint = getString(R.string.query_hint)
                return false
            }
        })
    }

    private fun createLyricWebViewFragment(): LyricWebViewFragment {
        var lyricWebViewFragment = supportFragmentManager.findFragmentByTag(
            TAG_LYRIC_WEBVIEW_FRAGMENT
        ) as LyricWebViewFragment?

        if (lyricWebViewFragment == null) {
            lyricWebViewFragment =
                LyricWebViewFragment.newInstance()
        }
        return lyricWebViewFragment
    }

    private fun showLyricWebViewFragment(lyricUrl: String) {
        val lyricWebViewFragment = createLyricWebViewFragment()
        lyricWebViewFragment.lyricUrl = lyricUrl
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_webview_placeholder, lyricWebViewFragment,
                TAG_LYRIC_WEBVIEW_FRAGMENT
            )
            .addToBackStack("LyricWebViewFragment")
            .commit()

        searchResultRecyclerView.visibility = View.GONE
    }

    private fun addBackStackListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                searchResultRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun showLoadingProgress() {
        val ghostLoadingViewGroup = findViewById<LinearLayout>(R.id.loadingSearchResults)
        ghostLoadingViewGroup.visibility = View.VISIBLE
        searchResultRecyclerView.visibility = View.GONE

    }

    private fun hideLoadingProgress() {
        val ghostLoadingViewGroup = findViewById<LinearLayout>(R.id.loadingSearchResults)
        ghostLoadingViewGroup.visibility = View.GONE
        searchResultRecyclerView.visibility = View.VISIBLE
    }
}

fun showError(context: Context, message: String) {
    AlertDialog.Builder(context)
        .setMessage(message)
        .setPositiveButton(context.getString(R.string.ok_button), null)
        .create()
        .show()
}