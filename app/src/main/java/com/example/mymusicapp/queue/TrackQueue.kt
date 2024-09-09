package com.example.mymusicapp.models

// Import your Track class if necessary
// import com.example.mymusicapp.models.Track

object TrackQueue {
    // Global variable to store the list of tracks as a queue
    val queue: MutableList<Track> = mutableListOf(
        Track(trackId = "1", albumId = "100", name = "Song One", duration = "03:15", path = "https://stream.nct.vn/Unv_Audio407/Style-TaylorSwift-12613800.mp3?st=pRySjZ0FyujVE8GCceCsJg&e=1726421423&a=1&p=0&r=c05c6a8a531da8ece839b15f044e408f&t=1725820265509"),
        Track(trackId = "2", albumId = "101", name = "Song Two", duration = "04:20", path = "https://stream.nct.vn/NhacCuaTui2045/MyLoveMineAllMine-Mitski-11792243.mp3?st=2dYuiSashTipIJfJL7dIkA&e=1726401569&a=1&p=0&r=a9e939d9f6a21270a02af0f99f3a90eb&t=1725820346977"),
        Track(trackId = "3", albumId = "102", name = "Song Three", duration = "02:45", path = "https://stream.nct.vn/Unv_Audio456/GetHimBack-OliviaRodrigo-14179007.mp3?st=zbIJ-BR9rlwfsgJ_R3xt_Q&e=1726421610&a=1&p=0&r=ebd3d7f20601e83585df2f6bf44c1ce9&t=1725820407447")
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
