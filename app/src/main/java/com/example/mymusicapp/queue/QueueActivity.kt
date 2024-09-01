package com.example.mymusicapp.queue

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.R

class QueueActivity : ComponentActivity() {
    private lateinit var recyclerViewQueue: RecyclerView
    private lateinit var recyclerViewPlaylist: RecyclerView
    private lateinit var queueSongAdapter: QueueSongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue)

        // Receive intent extras (if applicable)
        val imageUrl = intent.getStringExtra("IMAGE_URL") ?: "https://i.scdn.co/image/ab67616d0000b273356c22ef2466bb450b32e1bb"
        val songNameText = intent.getStringExtra("SONG_NAME") ?: "Default Song Name"
        val artistNameText = intent.getStringExtra("ARTIST_NAME") ?: "Default Artist Name"
        val playlistNameText = intent.getStringExtra("PLAYLIST_NAME") ?: "Default Playlist Name"

        // Update UI components
        val songCover = findViewById<ImageView>(R.id.songCover)
        val songName = findViewById<TextView>(R.id.songPlaying)
        val songArtist = findViewById<TextView>(R.id.artistSongPlaying)
        val playlistName = findViewById<TextView>(R.id.playlistName)

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

        recyclerViewQueue = findViewById(R.id.recyclerViewSongsQueue)
        recyclerViewQueue.layoutManager = LinearLayoutManager(this)
        queueSongAdapter = QueueSongAdapter(songs)
        recyclerViewQueue.adapter = queueSongAdapter

        recyclerViewPlaylist = findViewById(R.id.recyclerViewSongsPlaylist)
        recyclerViewPlaylist.layoutManager = LinearLayoutManager(this)
        queueSongAdapter = QueueSongAdapter(songs)
        recyclerViewPlaylist.adapter = queueSongAdapter
    }
}