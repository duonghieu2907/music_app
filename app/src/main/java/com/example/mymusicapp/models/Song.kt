package com.example.mymusicapp.models

// Song.kt
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val title: String,
    val artist: String,
    val imageUrl: String,
    val lyrics: String,
    val duration: String
) : Parcelable
