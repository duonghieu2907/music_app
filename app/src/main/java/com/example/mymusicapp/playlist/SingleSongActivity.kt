package com.example.mymusicapp.playlist

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mymusicapp.R

class SingleSongActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_song)

        // Retrieve the song from the Intent
        val song = intent.getParcelableExtra<Song>("song")

        // Update the UI elements with the song details
        if (song != null) {
            findViewById<TextView>(R.id.song_title).text = song.title
            findViewById<TextView>(R.id.artist_name).text = song.artist
            findViewById<TextView>(R.id.lyrics_text).text = song.lyrics
            findViewById<TextView>(R.id.song_end_time).text = song.duration

            // Load image using a library like Glide or Picasso
            val imageView = findViewById<ImageView>(R.id.song_cover)
            Glide.with(this).load(song.imageUrl).into(imageView)
        }
    }
}
