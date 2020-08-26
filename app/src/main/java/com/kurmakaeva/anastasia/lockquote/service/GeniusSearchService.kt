package com.kurmakaeva.anastasia.lockquote.service

import com.kurmakaeva.anastasia.lockquote.App
import com.kurmakaeva.anastasia.lockquote.R
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.OkHttpClient

private const val BASE_URL = "https://api.genius.com"

val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface GeniusSearchService {
    @GET("/search")

    fun searchSongByTerm(@Query("q") q: String): Call<GeniusSearchResponse>

    companion object {
        val instance: GeniusSearchService by lazy {

            val token = App.context?.resources?.getString(R.string.GENIUS_API_TOKEN)

            val client = OkHttpClient.Builder().addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }.build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .client(client)
                .build()

            retrofit.create(GeniusSearchService::class.java)
        }
    }
}