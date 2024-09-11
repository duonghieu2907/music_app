package com.example.mymusicapp.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.*

class PlaylistFragment : Fragment() {

    private lateinit var backButton: ImageView
    private lateinit var optionsButton: ImageView
    private lateinit var playlistImage: ImageView
    private lateinit var playlistTitle: TextView
    private lateinit var playlistCreator: TextView

    private lateinit var recyclerViewTracks: RecyclerView
    private lateinit var tracksAdapter: PlaylistTracksAdapter
    private var playlist: Playlist? = null
    private lateinit var curUserID: String
    private lateinit var dbHelper: MusicAppDatabaseHelper

    private var playlistId: String = ""  // Changed to String

    companion object {
        private const val ARG_PLAYLIST_ID = "playlist_id"
        private const val LIKED_SONGS_ID = "userLikedSongs" // -> compare arg to see if it is liked songs or plain playlist
        fun newInstance(playlistId: String): PlaylistFragment {
            val fragment = PlaylistFragment()
            val args = Bundle()
            args.putString(ARG_PLAYLIST_ID, playlistId)  // Changed to putString
            println(playlistId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            playlistId = it.getString(ARG_PLAYLIST_ID) ?: ""  // Changed to getString
        }
        curUserID = "1" // to be replaced
        dbHelper = MusicAppDatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_single_playlist, container, false)

        // Initialize UI
        backButton = view.findViewById(R.id.backButton)
        optionsButton = view.findViewById(R.id.optionsButton)
        playlistImage = view.findViewById(R.id.playlistImage)
        playlistTitle = view.findViewById(R.id.playlistTitle)
        playlistCreator = view.findViewById(R.id.playlistOwner)
        recyclerViewTracks = view.findViewById(R.id.recyclerViewSongs)

        // Set button click handlers
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        optionsButton.setOnClickListener {
            showOptionsMenu()
        }

        // RecyclerView
        recyclerViewTracks.layoutManager = LinearLayoutManager(requireContext())

        // Load data based on the playlist type
        if (playlistId == LIKED_SONGS_ID) {
            loadLikedSongs()
        } else {
            loadPlaylistData()
        }

        return view
    }

    private fun loadLikedSongs() {
        playlistTitle.text = "Liked Songs"
        playlistCreator.visibility = View.GONE

        Glide.with(this)
            .load(R.drawable.liked_songs_cover)
            .into(playlistImage)

        val trackList: List<Track> = dbHelper.getUserLikedTracks(curUserID)  // Use curUserID
        val likedSongsPlaylist = Playlist(
            playlistId = LIKED_SONGS_ID,
            userId = curUserID,
            name = "Liked Songs",
            image = ""
        )
        tracksAdapter = PlaylistTracksAdapter(trackList, dbHelper) { track -> openTrack(track, likedSongsPlaylist) }
        recyclerViewTracks.adapter = tracksAdapter
    }

    private fun loadPlaylistData() {
        // Fetch playlist details using playlistId
        playlist = dbHelper.getPlaylist(playlistId)

        if (playlist != null) {
            playlistTitle.text = playlist!!.name
            playlistCreator.text = dbHelper.getUser(playlist!!.userId)?.name ?: "Unknown"

            // Load playlist image
            Glide.with(this)
                .load(playlist!!.image)
                .placeholder(R.drawable.blacker_gradient)
                .into(playlistImage)

            // Fetch tracks for this playlist
            val trackList: List<Track> = dbHelper.getTracksByPlaylistId(playlistId)  // Ensure this method accepts String
            tracksAdapter = PlaylistTracksAdapter(trackList, dbHelper) { track -> openTrack(track, playlist!!) }
            recyclerViewTracks.adapter = tracksAdapter
        } else {
            Toast.makeText(requireContext(), "Playlist not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showOptionsMenu() {
        val popup = PopupMenu(requireContext(), optionsButton)
        val inflater: MenuInflater = popup.menuInflater

        when {
            // Case 1: Liked Songs playlist
            playlistId == LIKED_SONGS_ID -> {
                inflater.inflate(R.menu.playlist_options_menu_liked_songs, popup.menu)
            }

            // Case 2: Owned playlist
            playlist != null && playlist!!.userId == curUserID -> {
                inflater.inflate(R.menu.playlist_options_menu_own, popup.menu)
            }

            // Case 3: Follow/Unfollow for other users' playlists
            else -> {
                inflater.inflate(R.menu.playlist_options_menu_follow, popup.menu)

                // Check if the playlist is already followed by the user
                val isFollowed = dbHelper.isPlaylistFollowed(curUserID, playlist?.playlistId ?: "")

                // Change menu text based on the follow status
                popup.menu.findItem(R.id.action_follow_playlist)?.title =
                    if (isFollowed) "Unfollow Playlist" else "Follow Playlist"
            }
        }

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_edit_playlist -> {
                    // Implement edit playlist logic
                    println("Edit")
                }
                R.id.action_delete_playlist -> {
                    // Implement delete playlist logic
                    println("Delete")
                }
                R.id.action_add_to_queue -> {
                    // Implement add to queue logic
                    println("Queue")
                }
                R.id.action_share_playlist -> {
                    // Implement share playlist logic
                    println("Share")
                }
                R.id.action_follow_playlist -> {
                    // Follow or unfollow logic
                    if (dbHelper.isPlaylistFollowed(curUserID, playlist?.playlistId ?: "")) {
                        dbHelper.unfollowPlaylist(curUserID, playlist?.playlistId ?: "")
                        println("Unfollowed")
                    } else {
                        dbHelper.followPlaylist(curUserID, playlist?.playlistId ?: "")
                        println("Followed")
                    }
                }
                else -> false
            }
            true
        }
        popup.show()
    }

    private fun openTrack(track: Track, playlist: Playlist) {
        val fragment = SingleTrackFragment.newInstance(track, playlist)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        Toast.makeText(requireContext(), track.name, Toast.LENGTH_SHORT).show()
    }
}
