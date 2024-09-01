package com.example.mymusicapp.data

data class Track(
    val trackId: Int,
    val albumId: Int,
    val name: String,
    val duration: String, // Duration stored as string in format "MM:SS"
    val path: String // Path to the audio file
)
