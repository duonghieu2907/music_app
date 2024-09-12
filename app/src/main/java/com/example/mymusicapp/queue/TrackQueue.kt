package com.example.mymusicapp.models

// Import your Track class if necessary
// import com.example.mymusicapp.models.Track

object TrackQueue {
    // Global variable to store the list of tracks as a queue
    val queue: MutableList<Track> = mutableListOf(

    )


    // Function to add a track to the queue
    fun addTrack(track: Track) {
        queue.add(track)
    }




    // Function to add a list of tracks to the queue
    fun addTracks(tracks: List<Track>) {
        queue.addAll(tracks)
    }

    // Function to add a list of tracks to the beginning of the queue
    fun addTracksToBeginning(tracks: List<Track>) {
        queue.addAll(0, tracks)
    }

    // Function to clear the queue
    fun clearQueue() {
        queue.clear()
    }

    // Function to remove a specific track from the queue
    fun removeTrack(track: Track) {
        queue.remove(track)
    }
}
