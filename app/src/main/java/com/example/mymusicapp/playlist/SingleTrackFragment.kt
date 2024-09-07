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
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.queue.QueueFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerControlView

class SingleTrackFragment : Fragment() {

    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var track: Track

    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var songCover: ImageView
    private lateinit var playerControlView: PlayerControlView
    private lateinit var exoPlayer: ExoPlayer

    private lateinit var moreOptionsButton: ImageView

    private lateinit var likeButton: ImageView

    private lateinit var queueButton : ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_single_song, container, false)

        // Find the more_options button
        moreOptionsButton = view.findViewById(R.id.more_options)

        // Set click listener to open MenuFragment
        moreOptionsButton.setOnClickListener {
            openMenuFragment()
        }

        // Find the more_options button
        queueButton = view.findViewById(R.id.queue)

        // Set click listener to open QueueFragment
        queueButton.setOnClickListener {
            openQueueFragment()
        }

        // Initialize UI components
        songTitle = view.findViewById(R.id.song_title)
        artistName = view.findViewById(R.id.artist_name)
        songCover = view.findViewById(R.id.song_cover)
        playerControlView = view.findViewById(R.id.playerControlView)
        likeButton = view.findViewById(R.id.like)

        // Get the Track object passed from the previous fragment or activity
        track = arguments?.getParcelable("TRACK") ?: return view

        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Fetch Album and Artist details
        val album: Album? = dbHelper.getAlbum(track.albumId)  // Ensure getAlbum accepts String type
        val artist: Artist? = dbHelper.getArtist(album?.artistId ?: "")  // Ensure getArtist accepts String type

        // Set the UI components with track details
        songTitle.text = track.name
        artistName.text = artist?.name ?: "Unknown Artist"


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



        //Like button
        updateLikeButton()

        likeButton.setOnClickListener {
            toggleLikeStatus()
        }

        return view
    }

    private fun toggleLikeStatus() {
        if (dbHelper.isTrackLiked(track.trackId)) {
            dbHelper.deletePlaylistTrack("userLikedSong", track.trackId)
        } else {
            dbHelper.addPlaylistTrack("userLikedSong", track.trackId)
        }

        updateLikeButton()
    }

    private fun updateLikeButton() {
        if (dbHelper.isTrackLiked(track.trackId)) {
            likeButton.setImageResource(R.drawable.filledlove)  // Track is liked
        } else {
            likeButton.setImageResource(R.drawable.love)  // Track is not liked
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Release the player when the view is destroyed
        exoPlayer.release()
    }

    companion object {
        fun newInstance(track: Track, playlist: Playlist?): SingleTrackFragment {
            val fragment = SingleTrackFragment()
            val args = Bundle()
            args.putParcelable("TRACK", track)
            fragment.arguments = args
            return fragment
        }
    }

    private fun openMenuFragment() {
        val menuFragment = MenuFragment.newInstance(track) // Pass the track object

        // Replace the current fragment with the MenuFragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, menuFragment) // Ensure fragment_container is the correct ID
            .addToBackStack(null) // Optional, to add the fragment to the backstack
            .commit()
    }

    private fun openQueueFragment() {
        val queueFragment = QueueFragment.newInstance(track)

        // Replace the current fragment with the QueueFragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, queueFragment) // Ensure fragment_container is the correct ID
            .addToBackStack(null) // Optional, to add the fragment to the backstack
            .commit()
    }


}
