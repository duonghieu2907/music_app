package com.example.mymusicapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ExoPlayer

// ViewModel factory to pass ExoPlayer instance to PlayerViewModel
class PlayerViewModelFactory(private val exoPlayer: ExoPlayer,
    private val db: MusicAppDatabaseHelper, private val curUserId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(exoPlayer, db, curUserId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
