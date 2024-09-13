package com.example.mymusicapp.models

// Import your Track class if necessary
// import com.example.mymusicapp.models.Track

object TrackQueue {
    // Global variable to store the list of tracks as a queue
    val queue: MutableList<Track> = mutableListOf(

    )

    val queuePlaylistId: MutableList<String> = mutableListOf(

    )

    // Function to add a track to the queue
    fun addTrack(track: Track, playlistId: String) {
        queue.add(track)
        queuePlaylistId.add(playlistId)
    }




    // Function to add a list of tracks to the queue
    fun addTracks(tracks: List<Track>, playlistIds: List<String>?) {
        val playlistIdList = playlistIds?: listOf("")
        queue.addAll(tracks)
        queuePlaylistId.addAll(playlistIdList)
    }

    // Function to add a list of tracks to the beginning of the queue
    fun addTracksToBeginning(tracks: List<Track>) {
        queue.addAll(0, tracks)
    }

    // Function to clear the queue
    fun clearQueue() {
        queue.clear()
        queuePlaylistId.clear()
    }

    // Function to remove a specific track from the queue
    fun removeTrack(track: Track) {
        val index = queue.indexOf(track)
        queue.remove(track)
        queuePlaylistId.removeAt(index)
    }
}
