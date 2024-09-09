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
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Artist
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

    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var songCover: ImageView
    private lateinit var playerControlView: PlayerControlView
    private lateinit var exoPlayer: ExoPlayer

    private lateinit var moreOptionsButton: ImageView
    private lateinit var likeButton: ImageView
    private lateinit var queueButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_single_song, container, false)

        // Initialize dbHelper
        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Initialize UI components
        moreOptionsButton = view.findViewById(R.id.more_options)
        queueButton = view.findViewById(R.id.queue)
        songTitle = view.findViewById(R.id.song_title)
        artistName = view.findViewById(R.id.artist_name)
        songCover = view.findViewById(R.id.song_cover)
        playerControlView = view.findViewById(R.id.playerControlView)
        likeButton = view.findViewById(R.id.like)

        // Set click listener to open MenuFragment
        moreOptionsButton.setOnClickListener {
            openMenuFragment()
        }

        // Set click listener to open QueueFragment
        queueButton.setOnClickListener {
            openQueueFragment()
        }

        // Get the Track object passed from the previous fragment or activity
        track = arguments?.getParcelable("TRACK") ?: return view
        playlist = arguments?.getParcelable("PLAYLIST")


        track.path = "https://stream.nct.vn/NhacCuaTui2045/MyLoveMineAllMine-Mitski-11792243.mp3?st=2dYuiSashTipIJfJL7dIkA&e=1726401569&a=1&p=0&r=a9e939d9f6a21270a02af0f99f3a90eb&t=1725820346977"

        // Add the current track to the queue
        TrackQueue.queue.add(0, track)

        val testTracks: List<Track> = listOf(

            Track(trackId = "5", albumId = "104", name = "Song Five", duration = "03:30", path = "https://stream.nct.vn/NhacCuaTui2045/MyLoveMineAllMine-Mitski-11792243.mp3?st=2dYuiSashTipIJfJL7dIkA&e=1726401569&a=1&p=0&r=a9e939d9f6a21270a02af0f99f3a90eb&t=1725820346977"),
            Track(trackId = "6", albumId = "105", name = "Song Six", duration = "02:55", path = "https://stream.nct.vn/NhacCuaTui2045/MyLoveMineAllMine-Mitski-11792243.mp3?st=2dYuiSashTipIJfJL7dIkA&e=1726401569&a=1&p=0&r=a9e939d9f6a21270a02af0f99f3a90eb&t=1725820346977")
        )

        TrackQueue.addTracksToBeginning(testTracks)


        // Fetch Album and Artist details
        val album: Album? = dbHelper.getAlbum(track.albumId)
        val artist: Artist? = dbHelper.getArtist(album?.artistId ?: "")

        // Set the UI components with track details
        songTitle.text = track.name
        artistName.text = artist?.name ?: "Unknown Artist"

        // Load album cover image
        Glide.with(this)
            .load(album?.image)
            .placeholder(R.drawable.blacker_gradient)
            .into(songCover)

        // Initialize ExoPlayer
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        playerControlView.player = exoPlayer
        playerControlView.setShowTimeoutMs(0)

        // Start playing the first track in the queue
        playTrackAtIndex(0)

        // Add a listener for playback state changes to play the next track when current track finishes
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    // Playback finished, play the next track in the queue
                    playNextTrackInQueue()
                }
            }
        })


        // Like button functionality
        updateLikeButton()
        likeButton.setOnClickListener {
            toggleLikeStatus()
        }

        return view
    }

    private fun playTrackAtIndex(index: Int) {
        if (index >= TrackQueue.queue.size) return

        val currentTrack = TrackQueue.queue[index]

        // Check if the track's path is null or empty, and use a default URL if necessary
        val trackUrl = currentTrack.path


        val mediaItem = MediaItem.fromUri(Uri.parse(trackUrl))
        exoPlayer.setMediaItem(mediaItem)

        // Prepare the player and start playback
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        // Update UI for the current track
        songTitle.text = currentTrack.name
        val album = dbHelper.getAlbum(currentTrack.albumId)
        val artist = dbHelper.getArtist(album?.artistId ?: "")
        artistName.text = artist?.name ?: "Unknown Artist"

        Glide.with(this)
            .load(album?.image)
            .placeholder(R.drawable.blacker_gradient)
            .into(songCover)
    }

    private fun playNextTrackInQueue() {
        if (TrackQueue.queue.isNotEmpty()) {
            TrackQueue.queue.removeAt(0)
            if (TrackQueue.queue.isNotEmpty()) {
                playTrackAtIndex(0)
            } else {
                // No more tracks in the queue
                exoPlayer.stop()
                // Optionally, you can show a message like this:
                Toast.makeText(requireContext(), "End of the queue", Toast.LENGTH_SHORT).show()
            }
        }
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
            likeButton.setImageResource(R.drawable.filledlove)
        } else {
            likeButton.setImageResource(R.drawable.love)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer.release()
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
        val menuFragment = MenuFragment.newInstance(track)
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
}
