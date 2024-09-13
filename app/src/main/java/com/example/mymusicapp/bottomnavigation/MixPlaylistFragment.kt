package com.example.mymusicapp.bottomnavigation

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.TrackQueue
import com.example.mymusicapp.playlist.PlaylistTracksAdapter
import com.example.mymusicapp.playlist.SingleTrackFragment

class MixPlaylistFragment : Fragment() {

    private lateinit var backButton: ImageView
    private lateinit var optionsButton: ImageView
    private lateinit var playlistImage: View
    private lateinit var playlistImageUnderline: View
    private lateinit var playlistTitle: TextView
    private lateinit var playlistDescription: TextView

    private lateinit var recyclerViewTracks: RecyclerView
    private lateinit var tracksAdapter: PlaylistTracksAdapter
    private lateinit var dbHelper: MusicAppDatabaseHelper

    private var playlist: Playlist? = null
    private var playlistId: String = ""
    private var boxColor: Int = Color.TRANSPARENT
    private var underlineColor: Int = Color.TRANSPARENT
    private lateinit var curUserId: String

    companion object {
        private const val ARG_PLAYLIST_ID = "playlist_id"
        private const val LIKED_SONGS_ID = "userLikedSongs" // -> compare arg to see if it is liked songs or plain playlist
        fun newInstance(playlistId: String): MixPlaylistFragment {
            val fragment = MixPlaylistFragment()
            val args = Bundle()
            args.putString(ARG_PLAYLIST_ID, playlistId)
            println(playlistId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            playlistId = it.getString(ARG_PLAYLIST_ID) ?: ""
            boxColor = it.getInt("box_color", Color.TRANSPARENT)
            underlineColor = it.getInt("underline_color", Color.TRANSPARENT)
        }
        dbHelper = MusicAppDatabaseHelper(requireContext())
        if (isAdded) {
            val app = requireActivity().application as Global
            curUserId = app.curUserId
        } else {
            // fragment is not attached
            curUserId = "1"
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mix_playlist, container, false)

        // Hide the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }

        // Initialize UI
        backButton = view.findViewById(R.id.backButton)
        optionsButton = view.findViewById(R.id.optionsButton)
        playlistImage = view.findViewById(R.id.playlistImage)
        playlistImageUnderline = view.findViewById(R.id.underline)
        playlistTitle = view.findViewById(R.id.playlistTitle)
        playlistDescription = view.findViewById(R.id.playlistOwner)
        recyclerViewTracks = view.findViewById(R.id.recyclerViewSongs)

        //button
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        optionsButton.setOnClickListener {
            showOptionsMenu() // depends on the playlist type loaded below
        }

        // RecyclerView
        recyclerViewTracks.layoutManager = LinearLayoutManager(requireContext())

        // Load data
        loadPlaylistData()

        return view
    }

    private fun loadPlaylistData() {
        // Fetch playlist details
        playlist = dbHelper.getPlaylist(playlistId)

        if (playlist != null) {
            playlistTitle.text = playlist!!.name

            // Set playlist description based on the mix playlist
            playlistDescription.text = when (playlist!!.name) {
                "Mix 1" -> "A collection of the latest and greatest pop hits."
                "Mix 2" -> "Discover the newest tracks from the indie scene."
                "Mix 3" -> "Rock out to the best rock anthems."
                "Mix 4" -> "Enjoy alternative tracks that push the boundaries."
                "Mix 5" -> "Listen to the top Viet tracks of the moment."
                else -> ""
            }

            playlistImage.setBackgroundColor(boxColor)
            playlistImageUnderline.setBackgroundColor(underlineColor)

            // Fetch tracks
            val trackList: List<Track> = dbHelper.getTracksByPlaylistId(playlistId)

            tracksAdapter = PlaylistTracksAdapter(this, trackList, dbHelper, playlist!!.playlistId) { track -> openTrack(track, playlist!!) }

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
            playlist != null && playlist!!.userId == curUserId -> {
                inflater.inflate(R.menu.playlist_options_menu_own, popup.menu)
            }

            // Case 3: Follow/Unfollow for other users' playlists
            else -> {
                inflater.inflate(R.menu.playlist_options_menu_follow, popup.menu)

                val isFollowed = dbHelper.isPlaylistFollowed(curUserId, playlist?.playlistId ?: "")

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
                    // Show pop-up
                    showDeleteConfirmationDialog()
                    println("Delete")
                }
                R.id.action_add_to_queue -> {
                    // Implement add to queue logic

                    val trackList: List<Track>? = dbHelper.getTracksByPlaylistId(playlistId)

                    if (trackList != null) {
                        TrackQueue.addTracks(trackList)
                    } else {
                        Log.e("PlaylistFragment", "Track list is null for playlistId: $playlistId")
                    }
                    println("Queue")
                }
                R.id.action_share_playlist -> {
                    // Implement share playlist logic
                    println("Share")
                }
                R.id.action_follow_playlist -> {
                    // Follow or unfollow logic
                    if (dbHelper.isPlaylistFollowed(curUserId, playlist?.playlistId ?: "")) {
                        dbHelper.unfollowPlaylist(curUserId, playlist?.playlistId ?: "")
                        println("Unfollowed")
                    } else {
                        dbHelper.followPlaylist(curUserId, playlist?.playlistId ?: "")
                        println("Followed")
                    }
                }
                else -> false
            }
            true
        }
        popup.show()
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Playlist")
        builder.setMessage("Are you sure you want to delete this playlist? This action cannot be undone.")

        // delete
        builder.setPositiveButton("Yes") { dialog, _ ->
            deletePlaylist()
            dialog.dismiss()
        }

        // cancel
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }


        val dialog = builder.create()
        dialog.show()
    }

    private fun deletePlaylist() {
        val dbHelper = MusicAppDatabaseHelper(requireContext())
        val success = dbHelper.deletePlaylist(playlistId)

        if (success) {
            Toast.makeText(requireContext(), "Playlist deleted successfully.", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        } else {
            Toast.makeText(requireContext(), "Failed to delete the playlist.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openTrack(track: Track, playlist: Playlist) {
        val fragment = SingleTrackFragment.newInstance(track.trackId, playlist.playlistId, null)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        Toast.makeText(requireContext(), track.name, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // Hide the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }
    }
}
