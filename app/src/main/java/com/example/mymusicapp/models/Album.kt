package com.example.mymusicapp.models

data class Album(
    val albumId: String,
    val artistId: String,
    val name: String,
    val releaseDate: String,
    val image: String // Image stored as URL
)
