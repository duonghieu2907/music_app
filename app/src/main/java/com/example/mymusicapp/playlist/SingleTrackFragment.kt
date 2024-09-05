package com.example.mymusicapp.playlist

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Artist
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerControlView

class SingleTrackFragment : Fragment() {

    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var track: Track

    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var songCover: ImageView
    private lateinit var lyricsText: TextView
    private lateinit var songEndTime: TextView
    private lateinit var playerControlView: PlayerControlView
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_single_song, container, false)

        // Initialize UI components
        songTitle = view.findViewById(R.id.song_title)
        artistName = view.findViewById(R.id.artist_name)
        songCover = view.findViewById(R.id.song_cover)
        playerControlView = view.findViewById(R.id.playerControlView)

        // Get the Track object passed from the previous fragment or activity
        track = arguments?.getParcelable("TRACK") ?: return view

        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Fetch Album and Artist details
        val album: Album? = dbHelper.getAlbum(track.albumId)  // Ensure getAlbum accepts String type
        val artist: Artist? = dbHelper.getArtist(album?.artistId ?: "")  // Ensure getArtist accepts String type

        // Set the UI components with track details
        songTitle.text = track.name
        artistName.text = artist?.name ?: "Unknown Artist"
        lyricsText.text = "No lyrics available"
        songEndTime.text = track.duration

        // Load album cover image
        Glide.with(this)
            .load(album?.image)
            .placeholder(R.drawable.blacker_gradient) // Placeholder image if no image is available
            .into(songCover)

        // Initialize ExoPlayer
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        playerControlView.player = exoPlayer  // Attach the control view to the player

        // Prevent auto-hiding of the controls
        playerControlView.setShowTimeoutMs(0) // Set to 0 to always show controls

        // Load track URL into ExoPlayer
        val trackUrl =  "https://stream.nct.vn/Unv_Audio407/Style-TaylorSwift-12613800.mp3?st=HQN3Vju0fcNjatL9jDvorQ&e=1726036277&a=1&p=0&r=a0dad11b4823361cef20dccf5ac0758a&t=1725435123477"

        val mediaItem = MediaItem.fromUri(Uri.parse(trackUrl))
        exoPlayer.addMediaItem(mediaItem)

        // Prepare the player and start playback
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Release the player when the view is destroyed
        exoPlayer.release()
    }

    companion object {
        fun newInstance(track: Track): SingleTrackFragment {
            val fragment = SingleTrackFragment()
            val args = Bundle()
            args.putParcelable("TRACK", track)
            fragment.arguments = args
            return fragment
        }
    }
}
