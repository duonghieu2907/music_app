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
import com.bumptech.glide.Glide
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.TrackQueue
import com.example.mymusicapp.queue.QueueFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView

class SingleTrackFragment : Fragment() {

    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var track: Track
    private var playlist: Playlist? = null
    private lateinit var backButton: ImageView

    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var songCover: ImageView
    private lateinit var playerControlView: PlayerControlView
    private lateinit var exoPlayer: ExoPlayer

    private lateinit var moreOptionsButton: ImageView
    private lateinit var likeButton: ImageView
    private lateinit var queueButton: ImageView

    private lateinit var playlistName: TextView
    var currentTrackIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_single_song, container, false)

        // Hide the navigation bar when this fragment is created
        (requireActivity() as MainActivity).hideBottomNavigation()

        backButton = view.findViewById(R.id.back)

        // Set button click handlers
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Initialize ExoPlayer
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        playerControlView = view.findViewById(R.id.playerControlView)
        playerControlView.player = exoPlayer
        playerControlView.setShowTimeoutMs(0)

        // Listen for playback state changes to handle track completion
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    handleQueueCompletion()
                }
            }
        })

        moreOptionsButton = view.findViewById(R.id.more_options)
        queueButton = view.findViewById(R.id.queue)
        songTitle = view.findViewById(R.id.song_title)
        artistName = view.findViewById(R.id.artist_name)
        songCover = view.findViewById(R.id.song_cover)
        likeButton = view.findViewById(R.id.like)
        playlistName = view.findViewById(R.id.playlist_name)

        moreOptionsButton.setOnClickListener {
            openMenuFragment()
        }

        queueButton.setOnClickListener {
            openQueueFragment()
        }

        track = arguments?.getParcelable("TRACK") ?: return view
        playlist = arguments?.getParcelable("PLAYLIST")

        track.path = "https://stream.nct.vn/NhacCuaTui2045/MyLoveMineAllMine-Mitski-11792243.mp3?..."

        if (TrackQueue.queue.isEmpty()) {
            // Queue is empty, play this track but don't add to the queue
            playSingleTrackWithoutQueue()
        } else {
            // Check if track exists in the queue
            val trackIndex = TrackQueue.queue.indexOfFirst { it.trackId == track.trackId }
            if (trackIndex != -1) {
                // Start playing from the first occurrence of the track
                playTrackAtIndex(trackIndex)
            } else {
                // Add track to the queue and play it
                TrackQueue.queue.add(track)
                currentTrackIndex = TrackQueue.queue.size - 1
                playTrackAtIndex(currentTrackIndex)
            }
        }

        updateLikeButton()
        likeButton.setOnClickListener {
            toggleLikeStatus()
        }

        return view
    }

    private fun playSingleTrackWithoutQueue() {
        val mediaItem = MediaItem.fromUri(Uri.parse(track.path))
        exoPlayer.setMediaItem(mediaItem)

        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        songTitle.text = track.name
        val album = dbHelper.getAlbum(track.albumId)
        val artist = dbHelper.getArtist(album?.artistId ?: "")
        artistName.text = artist?.name ?: "Unknown Artist"

        playlistName.text = playlist?.name ?: ""


        playlistName.text = playlist?.name ?: ""

        Glide.with(this)
            .load(album?.image)
            .placeholder(R.drawable.blacker_gradient)
            .into(songCover)
    }

    private fun handleQueueCompletion() {
        if (TrackQueue.queue.isNotEmpty()) {
            // Continue to next track in the queue if there is any
            playNextTrackInQueue()
        } else {
            // Queue is empty, stop playback and wait for new songs to be added
            exoPlayer.stop()
            Toast.makeText(requireContext(), "Playback completed. Waiting for new tracks.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playTrackAtIndex(index: Int) {
        currentTrackIndex = index

        if (index >= TrackQueue.queue.size) return

        val currentTrack = TrackQueue.queue[index]
        val mediaItem = MediaItem.fromUri(Uri.parse(currentTrack.path))
        exoPlayer.setMediaItem(mediaItem)

        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        songTitle.text = currentTrack.name
        val album = dbHelper.getAlbum(currentTrack.albumId)
        val artist = dbHelper.getArtist(album?.artistId ?: "")
        artistName.text = artist?.name ?: "Unknown Artist"

        playlistName.text = playlist?.name ?: ""

        Glide.with(this)
            .load(album?.image)
            .placeholder(R.drawable.blacker_gradient)
            .into(songCover)
    }

    private fun playNextTrackInQueue() {
        currentTrackIndex++
        if (currentTrackIndex < TrackQueue.queue.size) {
            playTrackAtIndex(currentTrackIndex)
        } else {
            exoPlayer.stop()
            Toast.makeText(requireContext(), "End of the queue", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleLikeStatus() {
        if (dbHelper.isTrackLiked(track.trackId, "1")) {
            dbHelper.deleteLike("1", track.trackId ) // global  curUserID
        } else {
            dbHelper.addLike("1", track.trackId) // global  curUserID
        }
        updateLikeButton()
    }

    private fun updateLikeButton() {
        // is liked by the user
        val isLiked = dbHelper.isTrackLiked(track.trackId, "1")  // global  curUserID

        if (isLiked) {
            likeButton.setImageResource(R.drawable.filledlove)  // Filled heart for liked
        } else {
            likeButton.setImageResource(R.drawable.love)  // Empty heart for not liked
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer.release()
        // Show the navigation bar when this fragment is destroyed
        (requireActivity() as MainActivity).showBottomNavigation()
    }

    companion object {
        fun newInstance(track: Track, playlist: Playlist?): SingleTrackFragment {
            val fragment = SingleTrackFragment()
            val args = Bundle().apply {
                putParcelable("TRACK", track)
                if (playlist != null) {
                    putParcelable("PLAYLIST", playlist)
                }
            }
            fragment.arguments = args
            return fragment
        }
    }

    private fun openMenuFragment() {
        val menuFragment = MenuFragment.newInstance(track, playlist)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, menuFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun openQueueFragment() {
        val queueFragment = QueueFragment.newInstance(track, playlist)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, queueFragment)
            .addToBackStack(null)
            .commit()
    }



    override fun onResume() {
        super.onResume()
        // Hide the bottom navigation bar when this fragment is resumed
        (requireActivity() as MainActivity).hideBottomNavigation()
    }


}
