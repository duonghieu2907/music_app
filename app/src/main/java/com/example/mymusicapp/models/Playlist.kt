package com.example.mymusicapp.models

data class Playlist(
    val playlistId: Int,
    val userId: Int,
    val name: String,
    val image: String // Image stored as URL
)
