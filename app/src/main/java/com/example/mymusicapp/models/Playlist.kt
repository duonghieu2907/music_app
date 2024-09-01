package com.example.mymusicapp.models

data class Playlist(
    val playlistId: String,
    val userId: String,
    val name: String,
    val image: String // Image stored as URL
)
