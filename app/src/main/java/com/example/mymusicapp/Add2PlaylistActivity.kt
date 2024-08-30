package com.example.mymusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Add2PlaylistActivity : ComponentActivity() {

    private lateinit var playlistRecyclerView: RecyclerView
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_2_playlist)

        // Initialize RecyclerView
        playlistRecyclerView = findViewById(R.id.playlistRecyclerView)
        playlistRecyclerView.layoutManager = LinearLayoutManager(this)

        // Create an adapter and set it to the RecyclerView
        val playlists = listOf(
            PlaylistItem(R.drawable.current_favorites, "Current Favorites", "20 songs"),
            PlaylistItem(R.drawable.vibes, "3:00am vibes", "18 songs"),
            PlaylistItem(R.drawable.lofi_loft, "Lofi Loft", "63 songs"),
            PlaylistItem(R.drawable.rain_on_my_window, "Rain on My Window", "32 songs"),
            PlaylistItem(R.drawable.anime_osts, "Anime OSTs", "20 songs")
        )
        playlistAdapter = PlaylistAdapter(playlists)
        playlistRecyclerView.adapter = playlistAdapter
    }
}
