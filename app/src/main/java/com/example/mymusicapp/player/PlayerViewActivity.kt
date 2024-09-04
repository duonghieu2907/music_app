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

        // Example list of song URLs
        val songUrls = listOf(
            "https://stream.nct.vn/Unv_Audio407/Style-TaylorSwift-12613800.mp3?st=HQN3Vju0fcNjatL9jDvorQ&e=1726036277&a=1&p=0&r=a0dad11b4823361cef20dccf5ac0758a&t=1725435123477",
            "https://stream.nct.vn/Unv_Audio407/BlankSpace-TaylorSwift-12613798.mp3?st=ck1AgsECDmSwwKGlPallQw&e=1726060044&a=1&p=0&r=77e39ee3168f14ce514d9fbbd8dd8bd6&t=1725459229634",
            "https://stream.nct.vn/NhacCuaTui2045/MyLoveMineAllMine-Mitski-11792243.mp3?st=vsDeOPVGHrj-A6JvjYb5zw&e=1726060504&a=1&p=0&r=c6f6de9dc88576b90b245fd66609686e&t=1725459303067"
        )

        // Add each song URL as a MediaItem to the ExoPlayer playlist
        for (url in songUrls) {
            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            exoPlayer.addMediaItem(mediaItem)
        }

        // Prepare the player and start playback
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()  // Release the player when done
    }
}
