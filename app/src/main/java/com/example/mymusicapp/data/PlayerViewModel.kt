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

class PlayerViewModel(val exoPlayer: ExoPlayer) : ViewModel() {
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
}
