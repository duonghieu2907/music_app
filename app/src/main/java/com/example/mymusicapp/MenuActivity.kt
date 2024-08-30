package com.example.mymusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity

import androidx.activity.enableEdgeToEdge

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.mymusicapp.playlist.Add2PlaylistActivity
import com.example.mymusicapp.queue.QueueActivity

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)



        // Find views by ID
        val songCover = findViewById<ImageView>(R.id.songCover)
        val songName = findViewById<TextView>(R.id.songName)
        val songArtist = findViewById<TextView>(R.id.songArtist)

        // Set the values for the songCover, songName, and songArtist
        songCover.setImageResource(R.drawable.anime_osts) // Replace with your actual image resource
        songName.text = "lofi loft" // Replace with the actual song name
        songArtist.text = "New Artist Name" // Replace with the actual artist name

        // Find views by ID for the menu items
        val addPlaylistIcon = findViewById<ImageView>(R.id.addPlaylist)
        val add2PlaylistText = findViewById<TextView>(R.id.add2Playlist)

        // Set click listeners for the menu items
        val clickListener = View.OnClickListener {
            val intent = Intent(this, Add2PlaylistActivity::class.java)
            startActivity(intent)
        }
  
        // Find views by ID for the menu items
        val addQIcon = findViewById<ImageView>(R.id.addQ)
        val add2QText = findViewById<TextView>(R.id.add2Q)

        // Set click listeners for the menu items
        val clickListener2 = View.OnClickListener {
            val intent = Intent(this, QueueActivity::class.java)
            startActivity(intent)
        }


        addPlaylistIcon.setOnClickListener(clickListener)
        add2PlaylistText.setOnClickListener(clickListener)

        addQIcon.setOnClickListener(clickListener2)
        add2QText.setOnClickListener(clickListener2)
    }
}
