package com.example.mymusicapp.player

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
import com.example.mymusicapp.player.ui.theme.MyMusicAppTheme
import android.net.Uri
import android.widget.Button
import com.example.mymusicapp.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

class PlayerViewActivity : ComponentActivity() {
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_view)

        val playerView = findViewById<PlayerView>(R.id.playerView)
        val playButton = findViewById<Button>(R.id.playButton)

        // Initialize ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        // Set the media item to be played
        val mediaItem = MediaItem.fromUri(Uri.parse("https://files.freemusicarchive.org/storage-freemusicarchive-org/tribe-of-noise-pro/music/6ec8ccb72e97ef2a98aa92d0f231b36f.mp3"))
        exoPlayer.setMediaItem(mediaItem)

        playButton.setOnClickListener {
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()  // Release the player when done
    }
}