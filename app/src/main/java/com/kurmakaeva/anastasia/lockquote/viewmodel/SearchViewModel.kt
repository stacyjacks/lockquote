package com.kurmakaeva.anastasia.lockquote.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kurmakaeva.anastasia.lockquote.repository.GeniusRepo
import com.kurmakaeva.anastasia.lockquote.service.GeniusHitResult


class SearchViewModel(application: Application): AndroidViewModel(application) {
    var geniusRepo: GeniusRepo? = null

    data class SongSummaryViewData(
        val id: Long? = 0,
        val api_path: String? = "",
        val path: String? = "",
        val title: String? = "",
        val header_image_thumbnail_url: String? = "",
        val name: String = ""
    )

    private fun songToSongSummaryView(geniusSong: GeniusHitResult): SongSummaryViewData {
        return SongSummaryViewData(
            geniusSong.id,
            geniusSong.api_path,
            geniusSong.path,
            geniusSong.title,
            geniusSong.header_image_thumbnail_url,
            geniusSong.primary_artist.name
        )
    }

    fun searchSongs(term: String, callback: (List<SongSummaryViewData>) -> Unit) {
        geniusRepo?.searchByTerm(term) { results ->
            if (results == null) {
                callback(emptyList())
            } else {
                    val searchViews = results.map { geniusSong ->
                    songToSongSummaryView(geniusSong)
                }
                callback(searchViews)
            }
        }
    }
}

