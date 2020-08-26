package com.kurmakaeva.anastasia.lockquote.repository

import com.kurmakaeva.anastasia.lockquote.service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GeniusRepo(private val geniusSearchService: GeniusSearchService) {
    fun searchByTerm(term: String, callback: (List<GeniusHitResult>?) -> Unit) {
        val songSearchCall = geniusSearchService.searchSongByTerm(term)
        songSearchCall.enqueue(object: Callback<GeniusSearchResponse> {
            override fun onFailure(call: Call<GeniusSearchResponse>, t: Throwable) {
                callback(null)
            }

            override fun onResponse(call: Call<GeniusSearchResponse>?, response: Response<GeniusSearchResponse>?) {
                val body = response?.body()
                callback(body?.response?.hits?.map { it.result })
            }
        })
    }
}