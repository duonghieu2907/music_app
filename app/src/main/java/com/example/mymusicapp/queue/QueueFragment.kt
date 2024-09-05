package com.example.mymusicapp.queue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.R

class QueueFragment : Fragment() {

    private lateinit var recyclerViewQueue: RecyclerView
    private lateinit var recyclerViewPlaylist: RecyclerView
    private lateinit var queueSongAdapter: QueueSongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_queue, container, false)

        // Receive arguments passed to this fragment (if applicable)
        val imageUrl = arguments?.getString("IMAGE_URL") ?: "https://i.scdn.co/image/ab67616d0000b273356c22ef2466bb450b32e1bb"
        val songNameText = arguments?.getString("SONG_NAME") ?: "Default Song Name"
        val artistNameText = arguments?.getString("ARTIST_NAME") ?: "Default Artist Name"
        val playlistNameText = arguments?.getString("PLAYLIST_NAME") ?: "Default Playlist Name"

        // Update UI components
        val songCover = view.findViewById<ImageView>(R.id.songCover)
        val songName = view.findViewById<TextView>(R.id.songPlaying)
        val songArtist = view.findViewById<TextView>(R.id.artistSongPlaying)
        val playlistName = view.findViewById<TextView>(R.id.playlistName)

        Glide.with(this).load(imageUrl).into(songCover)
        songName.text = songNameText
        songArtist.text = artistNameText
        playlistName.text = playlistNameText

        // Set up RecyclerViews
        val songs = listOf(
            QueueSong("Song 1", "Artist 1"),
            QueueSong("Song 2", "Artist 2"),
            QueueSong("Song 3", "Artist 3"),
            // Add more songs here
        )

        recyclerViewQueue = view.findViewById(R.id.recyclerViewSongsQueue)
        recyclerViewQueue.layoutManager = LinearLayoutManager(requireContext())
        queueSongAdapter = QueueSongAdapter(songs)
        recyclerViewQueue.adapter = queueSongAdapter

        recyclerViewPlaylist = view.findViewById(R.id.recyclerViewSongsPlaylist)
        recyclerViewPlaylist.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewPlaylist.adapter = queueSongAdapter

        return view
    }
}
