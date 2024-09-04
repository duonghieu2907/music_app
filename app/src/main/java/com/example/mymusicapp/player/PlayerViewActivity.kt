package com.example.mymusicapp.player

import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import com.example.mymusicapp.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerControlView

class PlayerViewActivity : ComponentActivity() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerControlView: PlayerControlView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_song)

        playerControlView = findViewById(R.id.playerControlView)

        // Initialize ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        playerControlView.player = exoPlayer  // Attach the control view to the player

        // Prevent auto-hiding of the controls
        playerControlView.setShowTimeoutMs(0) // Set to 0 to always show controls

        // Set the media item to be played
        val mediaItem = MediaItem.fromUri(
            Uri.parse("https://stream.nct.vn/Unv_Audio407/Style-TaylorSwift-12613800.mp3?st=HQN3Vju0fcNjatL9jDvorQ&e=1726036277&a=1&p=0&r=a0dad11b4823361cef20dccf5ac0758a&t=1725435123477")
        )
        exoPlayer.setMediaItem(mediaItem)

        // Prepare and start playback
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()  // Release the player when done
    }
}
