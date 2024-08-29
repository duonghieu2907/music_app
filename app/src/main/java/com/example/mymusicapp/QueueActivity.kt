package com.example.mymusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymusicapp.ui.theme.MyMusicAppTheme
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyMusicAppTheme {
        Greeting("Android")
    }
}