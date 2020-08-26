package com.kurmakaeva.anastasia.lockquote.service

data class GeniusSearchResponse(
    val response: GeniusResponse
)

data class GeniusResponse(
    val hits: List<GeniusHit>
)

data class GeniusHit(
    val result: GeniusHitResult
)

data class GeniusHitResult(
    val id: Long,
    val api_path: String,
    val path: String,
    val title: String,
    val header_image_thumbnail_url: String,
    val primary_artist: GeniusPrimaryArtist
)

data class GeniusPrimaryArtist(
    val name: String
)