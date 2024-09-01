package com.example.mymusicapp.models

data class Album(
    val albumId: Int,
    val artistId: Int,
    val name: String,
    val releaseDate: String,
    val image: String // Image stored as URL
)
