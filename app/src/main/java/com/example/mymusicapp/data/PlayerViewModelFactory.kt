package com.example.mymusicapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ExoPlayer

// ViewModel factory to pass ExoPlayer instance to PlayerViewModel
class PlayerViewModelFactory(private val exoPlayer: ExoPlayer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(exoPlayer) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
