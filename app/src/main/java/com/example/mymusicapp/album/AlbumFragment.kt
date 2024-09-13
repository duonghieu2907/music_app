package com.example.mymusicapp.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.TrackQueue
import com.example.mymusicapp.playlist.SingleTrackFragment

class AlbumFragment : Fragment() {

    private lateinit var backButton: ImageView
    private lateinit var optionsButton: ImageView
    private lateinit var albumImage: ImageView
    private lateinit var albumTitle: TextView
    private lateinit var albumArtist: TextView

    private lateinit var recyclerViewTracks: RecyclerView
    private lateinit var tracksAdapter: AlbumTracksAdapter
    private lateinit var dbHelper: MusicAppDatabaseHelper

    private var albumId: String = ""
    private lateinit var curUserId: String

    companion object {
        private const val ARG_ALBUM_ID = "album_id"

        fun newInstance(albumId: String): AlbumFragment {
            val fragment = AlbumFragment()
            val args = Bundle()
            args.putString(ARG_ALBUM_ID, albumId)
            println(albumId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            albumId = it.getString(ARG_ALBUM_ID) ?: ""
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
        val view = inflater.inflate(R.layout.fragment_single_album, container, false)

        // Initialize UI
        backButton = view.findViewById(R.id.backButton)
        optionsButton = view.findViewById(R.id.optionsButton)
        albumImage = view.findViewById(R.id.albumImage)
        albumTitle = view.findViewById(R.id.albumTitle)
        albumArtist = view.findViewById(R.id.artistName)
        recyclerViewTracks = view.findViewById(R.id.recyclerViewSongs)

        // Set button click handlers
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        optionsButton.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.album_options_menu, popup.menu)

            val isFollowed = dbHelper.isAlbumFollowed(curUserId, albumId)  // Assuming you have a method to check

            // Update the Follow/Unfollow menu item based on the follow status
            popup.menu.findItem(R.id.action_follow_album).title = if (isFollowed) {
                "Unfollow Album"
            } else {
                "Follow Album"
            }

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_add_to_queue -> {
                        // Implement add to queue logic

                        val trackList: List<Track> = dbHelper.getTracksByAlbumId(albumId)  // Ensure this method accepts String

                        TrackQueue.addTracks(trackList, null)
                        println("Queue")
                        true
                    }
                    R.id.action_share_album -> {
                        // Implement share album logic
                        println("Share Album")
                        true
                    }
                    R.id.action_follow_album -> {
                        if (isFollowed) {
                            // unfollow album
                            dbHelper.unfollowAlbum(curUserId, albumId)
                            Toast.makeText(requireContext(), "Album Unfollowed", Toast.LENGTH_SHORT).show()
                        } else {
                            // follow album
                            dbHelper.followAlbum(curUserId, albumId)
                            Toast.makeText(requireContext(), "Album Followed", Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }



        // RecyclerView
        recyclerViewTracks.layoutManager = LinearLayoutManager(requireContext())

        loadAlbumData()

        return view
    }



    private fun loadAlbumData() {
        // Fetch album details using albumId
        val album: Album? = dbHelper.getAlbum(albumId)

        if (album != null) {
            println("Success: " + albumId)
            albumTitle.text = album.name
            albumArtist.text = dbHelper.getArtist(album.artistId)?.name ?:  "Unknown"

            // album image
            Glide.with(this)
                .load(album.image)
                .placeholder(R.drawable.blacker_gradient)
                .into(albumImage)

            // Fetch tracks for this album
            val trackList: List<Track> = dbHelper.getTracksByAlbumId(albumId)  // Ensure this method accepts String
            tracksAdapter = AlbumTracksAdapter(trackList, dbHelper) { track -> openTrack(track.trackId, albumId) }
            recyclerViewTracks.adapter = tracksAdapter
        } else {
            Toast.makeText(requireContext(), "Album not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openTrack(trackId: String, albumId: String? = null) {
        val fragment = SingleTrackFragment.newInstance(trackId,null, albumId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        Toast.makeText(requireContext(), trackId, Toast.LENGTH_SHORT).show()
    }
}
