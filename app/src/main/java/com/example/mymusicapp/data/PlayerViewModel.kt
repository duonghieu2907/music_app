package com.example.mymusicapp.data


import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.TrackQueue
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

class PlayerViewModel(val exoPlayer: ExoPlayer, val db: MusicAppDatabaseHelper, val curUserId: String) : ViewModel() {
    private val _currentTrack = MutableLiveData<Track>()
    private val _currentPlaylistId = MutableLiveData<String>()
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

    fun playTrack(track: Track, playlistId: String) {
        _currentTrack.value = track // Notify observers of the track change
        _currentPlaylistId.value = playlistId

        //Record
        db.addHistory(curUserId, playlistId, track.trackId)

        if (track.path.startsWith( "SpotifyUri")) {
            track.path = "https://stream.nct.vn/NhacCuaTui2045/MyLoveMineAllMine-Mitski-11792243.mp3?..."
        }


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
            val nextPlaylistId = TrackQueue.queuePlaylistId[currentTrackIndex]
            playTrack(nextTrack, nextPlaylistId)
        } else {
            exoPlayer.stop() // End of the queue
        }
    }

    fun playPreviousTrackInQueue() {
        if (currentTrackIndex > 0) {
            currentTrackIndex--
            val previousTrack = TrackQueue.queue[currentTrackIndex]
            val previousPlaylistId = TrackQueue.queuePlaylistId[currentTrackIndex]
            playTrack(previousTrack, previousPlaylistId)
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
            _currentTrack.value = TrackQueue.queue.get(currentTrackIndex)
            _currentPlaylistId.value = TrackQueue.queuePlaylistId.get(currentTrackIndex)

            val track = TrackQueue.queue[index] // Get the track at the specified index
            val playlistId = TrackQueue.queuePlaylistId[index]

            db.addHistory(curUserId, playlistId, track.trackId)

            if (track.path.startsWith( "SpotifyUri")) {
                track.path = "https://stream.nct.vn/NhacCuaTui2045/MyLoveMineAllMine-Mitski-11792243.mp3?..."
            }

            // Prepare the ExoPlayer to play the track directly
            val mediaItem = MediaItem.fromUri(Uri.parse(track.path))
            exoPlayer.setMediaItem(mediaItem) // Set the media item
            exoPlayer.prepare() // Prepare the player
            exoPlayer.playWhenReady = true // Start playback

        } else {
            // Handle the case where the index is out of bounds
            exoPlayer.stop() // Stop playback if the index is invalid
        }
    }


    fun isTrackCurrentlyPlaying(trackId: String): Boolean {
        return _currentTrack.value?.trackId == trackId
    }


}
