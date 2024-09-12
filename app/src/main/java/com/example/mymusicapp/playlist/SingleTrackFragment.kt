package com.example.mymusicapp.playlist

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.TrackQueue
import com.example.mymusicapp.queue.QueueFragment
import com.example.mymusicapp.data.PlayerViewModel
import com.example.mymusicapp.data.PlayerViewModelFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.SimpleExoPlayer

class SingleTrackFragment : Fragment() {

    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var track: Track
    private var playlist: Playlist? = null
    private var album: Album? = null
    private lateinit var backButton: ImageView
    private lateinit var previousButton: ImageView

    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var songCover: ImageView
    private lateinit var playingFrom: TextView
    private lateinit var playerControlView: PlayerControlView
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var exoPlayer: ExoPlayer

    private lateinit var moreOptionsButton: ImageView
    private lateinit var likeButton: ImageView
    private lateinit var queueButton: ImageView

    private lateinit var playlistName: TextView
    private lateinit var curUserId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_single_song, container, false)

        // Initialize ExoPlayer
        exoPlayer = ExoPlayer.Builder(requireContext()).build()

        // Hide the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }

        // Initialize ViewModel using the factory, passing ExoPlayer to it
        playerViewModel = ViewModelProvider(
            requireActivity(), PlayerViewModelFactory(exoPlayer)
        ).get(PlayerViewModel::class.java)

        backButton = view.findViewById(R.id.back)
        previousButton = view.findViewById(R.id.previous_button)
        dbHelper = MusicAppDatabaseHelper(requireContext())

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        previousButton.setOnClickListener {
            playerViewModel.playPreviousTrackInQueue()
        }

        // Get global user ID
        val app = requireActivity().application as Global
        curUserId = app.curUserId

        // Set the ExoPlayer in the PlayerControlView from ViewModel
        playerControlView = view.findViewById(R.id.playerControlView)
        playerControlView.player = playerViewModel.exoPlayer
        playerControlView.setShowTimeoutMs(0)

        moreOptionsButton = view.findViewById(R.id.more_options)
        queueButton = view.findViewById(R.id.queue)
        songTitle = view.findViewById(R.id.song_title)
        artistName = view.findViewById(R.id.artist_name)
        playingFrom = view.findViewById(R.id.playing_from_playlist)
        songCover = view.findViewById(R.id.song_cover)
        likeButton = view.findViewById(R.id.like)
        playlistName = view.findViewById(R.id.playlist_name)

        moreOptionsButton.setOnClickListener {
            openMenuFragment()
        }

        queueButton.setOnClickListener {
            openQueueFragment()
        }

        // Fetch track, playlist, and album using IDs
        val trackId = arguments?.getString("TRACK_ID") ?: return view
        val playlistId = arguments?.getString("PLAYLIST_ID")
        val albumId = arguments?.getString("ALBUM_ID")

        track = dbHelper.getTrack(trackId) ?: return view
        playlist = playlistId?.let { dbHelper.getPlaylist(it) }
        album = albumId?.let { dbHelper.getAlbum(it) }

        if (TrackQueue.queue.isEmpty()) {
            playerViewModel.playTrack(track) // Play track without adding to queue
        } else {
            val trackIndex = TrackQueue.queue.indexOfFirst { it.trackId == track.trackId }
            if (trackIndex != -1) {
                playerViewModel.playTrack(TrackQueue.queue[trackIndex]) // Play existing track
            } else {
                TrackQueue.queue.add(track)
                playerViewModel.playTrack(track) // Add track to queue and play it
            }
        }

        updateLikeButton()
        likeButton.setOnClickListener {
            toggleLikeStatus()
        }

        return view
    }

    private fun updateLikeButton() {
        val isLiked = dbHelper.isTrackLiked(track.trackId, curUserId)

        if (isLiked) {
            likeButton.setImageResource(R.drawable.filledlove)
        } else {
            likeButton.setImageResource(R.drawable.love)
        }
    }

    private fun toggleLikeStatus() {
        if (dbHelper.isTrackLiked(track.trackId, curUserId)) {
            dbHelper.deleteLike(curUserId, track.trackId)
        } else {
            dbHelper.addLike(curUserId, track.trackId)
        }
        updateLikeButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.showBottomNavigation()
        }
    }

    companion object {
        fun newInstance(trackId: String, playlistId: String? = null, albumId: String? = null): SingleTrackFragment {
            val fragment = SingleTrackFragment()
            val args = Bundle()
            args.putString("TRACK_ID", trackId)
            playlistId?.let { args.putString("PLAYLIST_ID", it) }
            albumId?.let { args.putString("ALBUM_ID", it) }
            fragment.arguments = args
            return fragment
        }
    }

    private fun openMenuFragment() {
        val menuFragment = MenuFragment.newInstance(track.trackId, playlist?.playlistId)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, menuFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun openQueueFragment() {
        val queueFragment = QueueFragment.newInstance(track, playlist, album)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, queueFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }
    }
}
