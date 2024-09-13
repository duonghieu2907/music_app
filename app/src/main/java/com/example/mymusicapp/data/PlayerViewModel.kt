package com.example.mymusicapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.TrackQueue

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player


import com.google.android.exoplayer2.ExoPlayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PlayerViewModel(val exoPlayer: ExoPlayer) : ViewModel() {

    private val _currentTrack = MutableLiveData<Track>()
    val currentTrack: LiveData<Track> get() = _currentTrack


    var currentTrackIndex = 0

    init {
        // Setup ExoPlayer listeners
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    handleQueueCompletion()
                }
            }
        })
    }

    fun playTrack(track: Track) {

        _currentTrack.value = track // Notify observers of the track change

        val mediaItem = MediaItem.fromUri(Uri.parse(track.path))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    private fun handleQueueCompletion() {
        if (TrackQueue.queue.isNotEmpty()) {
            // Move to the next track in the queue
            playNextTrackInQueue()
        } else {
            exoPlayer.stop()
        }
    }

    fun playNextTrackInQueue() {
        currentTrackIndex++
        if (currentTrackIndex < TrackQueue.queue.size) {
            val nextTrack = TrackQueue.queue[currentTrackIndex]
            playTrack(nextTrack)
        } else {
            exoPlayer.stop() // End of the queue
        }
    }

    fun playPreviousTrackInQueue() {
        if (currentTrackIndex > 0) {
            currentTrackIndex--
            val previousTrack = TrackQueue.queue[currentTrackIndex]
            playTrack(previousTrack)
        }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }


    fun playTrackAtIndex(index: Int) {
        // Check if the index is within the bounds of the queue
        if (index >= 0 && index < TrackQueue.queue.size) {
            currentTrackIndex = index // Update the current track index

            val track = TrackQueue.queue[index] // Get the track at the specified index

            // Prepare the ExoPlayer to play the track directly
            val mediaItem = MediaItem.fromUri(Uri.parse(track.path))
            exoPlayer.setMediaItem(mediaItem) // Set the media item
            exoPlayer.prepare() // Prepare the player
            exoPlayer.playWhenReady = true // Start playback

            // Reset the ExoPlayer listener to avoid advancing in the queue automatically
            exoPlayer.clearMediaItems() // Clear any queued media items to ensure only this track is played
        } else {
            // Handle the case where the index is out of bounds
            exoPlayer.stop() // Stop playback if the index is invalid
        }
    }

}
