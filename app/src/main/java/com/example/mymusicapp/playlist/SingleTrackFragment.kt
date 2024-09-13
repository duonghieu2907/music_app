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
import com.example.mymusicapp.data.PlayerViewModel
import com.example.mymusicapp.data.PlayerViewModelFactory
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.TrackQueue
import com.example.mymusicapp.queue.QueueFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerControlView

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

        // Initialize ExoPlayer and Database
        dbHelper = MusicAppDatabaseHelper(requireContext())
        exoPlayer = ExoPlayer.Builder(requireContext()).build()

        // Get global user ID
        val app = requireActivity().application as Global
        curUserId = app.curUserId

        // Hide the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }

        // Initialize ViewModel using the factory, passing ExoPlayer to it
        playerViewModel = ViewModelProvider(
            requireActivity(), PlayerViewModelFactory(exoPlayer, dbHelper, curUserId)
        ).get(PlayerViewModel::class.java)

        // Observe currentTrack LiveData for track changes
        playerViewModel.currentTrack.observe(viewLifecycleOwner) { track ->
            updateUIForTrack(track)
        }

        backButton = view.findViewById(R.id.back)
        previousButton = view.findViewById(R.id.previous_button)

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        previousButton.setOnClickListener {
            playerViewModel.playPreviousTrackInQueue()
        }

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
        val playlistId = arguments?.getString("PLAYLIST_ID")?: ""
        val albumId = arguments?.getString("ALBUM_ID")

        track = dbHelper.getTrack(trackId) ?: return view
        playlist = playlistId.let { dbHelper.getPlaylist(it) }
        album = albumId?.let { dbHelper.getAlbum(it) }

        if (TrackQueue.queue.isEmpty()) {
            playerViewModel.playTrack(track, playlistId) // Play track without adding to queue
        } else {
            val trackIndex = TrackQueue.queue.indexOfFirst { it.trackId == track.trackId }
            if (trackIndex != -1) {
                playerViewModel.playTrackAtIndex(trackIndex - 1) // Play existing track
            } else {
                TrackQueue.addTrack(track, playlistId)
                playerViewModel.playTrackAtIndex(TrackQueue.queue.size - 1) // Add track to queue and play it
                // This case got  bug
            }
        }

        updateLikeButton()
        likeButton.setOnClickListener {
            toggleLikeStatus()
        }



        songTitle.text = track.name
        val album1 = dbHelper.getAlbum(track.albumId)
        val artist = dbHelper.getArtist(album1?.artistId ?: "")
        artistName.text = artist?.name ?: "Unknown Artist"


        if(playlist != null)
        {
            playlistName.text = playlist?.name
            playingFrom.text = "PLAYING FROM PLAYLIST"
        }
        else if (album != null)
        {
            playlistName.text = album?.name
            playingFrom.text = "PLAYING FROM ALBUM"
        }
        else {
            playlistName.text = ""
            playingFrom.text = ""
        }


        Glide.with(this)
            .load(album1?.image)
            .placeholder(R.drawable.blacker_gradient)
            .into(songCover)


        previousButton.setOnClickListener {
            if (playerViewModel.currentTrackIndex > 0) {
                playerViewModel.currentTrackIndex-- // Decrement index in ViewModel
                playTrackAtIndex(playerViewModel.currentTrackIndex) // Use updated index
                dbHelper.addHistory(curUserId, playlistId, trackId)

            } else {
                Toast.makeText(requireContext(), "Already at the first track in the queue", Toast.LENGTH_SHORT).show()
            }
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

    private fun playTrackAtIndex(index: Int) {
        playerViewModel.currentTrackIndex = index // Update index in ViewModel

        if (index >= TrackQueue.queue.size) return

        val currentTrack = TrackQueue.queue[index]

        // Play the selected track using the ViewModel's ExoPlayer
        val mediaItem = MediaItem.fromUri(Uri.parse(currentTrack.path))
        playerViewModel.exoPlayer.setMediaItem(mediaItem)
        playerViewModel.exoPlayer.prepare()
        playerViewModel.exoPlayer.playWhenReady = true

        // Update UI
        songTitle.text = currentTrack.name
        val album1 = dbHelper.getAlbum(currentTrack.albumId)
        val artist = dbHelper.getArtist(album1?.artistId ?: "")
        artistName.text = artist?.name ?: "Unknown Artist"

        Glide.with(this)
            .load(album1?.image)
            .placeholder(R.drawable.blacker_gradient)
            .into(songCover)
    }


    private fun updateUIForTrack(track: Track) {
        songTitle.text = track.name
        val album1 = dbHelper.getAlbum(track.albumId)
        val artist = dbHelper.getArtist(album1?.artistId ?: "")
        artistName.text = artist?.name ?: "Unknown Artist"

        Glide.with(this)
            .load(album1?.image)
            .placeholder(R.drawable.blacker_gradient)
            .into(songCover)
    }

}
