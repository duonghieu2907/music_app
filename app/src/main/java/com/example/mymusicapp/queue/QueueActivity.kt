package com.example.mymusicapp.queue

import android.os.Bundle
import androidx.activity.ComponentActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R

class QueueActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var songAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue)

        val songs = listOf(
            Song("Song 1", "Artist 1"),
            Song("Song 2", "Artist 2"),
            Song("Song 3", "Artist 3"),
            // Add more songs here
        )

        recyclerView = findViewById(R.id.recyclerViewSongsQueue)
        recyclerView.layoutManager = LinearLayoutManager(this)
        songAdapter = SongAdapter(songs)
        recyclerView.adapter = songAdapter


        recyclerView = findViewById(R.id.recyclerViewSongsPlaylist)
        recyclerView.layoutManager = LinearLayoutManager(this)
        songAdapter = SongAdapter(songs)
        recyclerView.adapter = songAdapter
    }
}
