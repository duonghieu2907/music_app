package com.example.mymusicapp.models

data class SearchResult(
    val id: String,
    val name: String,
    val type: String,
    val image: String?, // URI to the cover image
    val subName: String? // Artist name for albums, Owner name for playlists
)


