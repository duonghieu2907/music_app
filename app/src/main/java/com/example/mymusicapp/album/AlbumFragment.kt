package com.example.mymusicapp.album

import android.app.AlertDialog
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

    private var albumId: String = ""  // Changed to String

    companion object {
        private const val ARG_ALBUM_ID = "album_id"

        fun newInstance(albumId: String): AlbumFragment {
            val fragment = AlbumFragment()
            val args = Bundle()
            args.putString(ARG_ALBUM_ID, albumId)  // Changed to putString
            println(albumId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            albumId = it.getString(ARG_ALBUM_ID) ?: ""  // Changed to getString
        }

        dbHelper = MusicAppDatabaseHelper(requireContext())
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
            /*inflater.inflate(R.menu.album_options_menu, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit_album -> {
                        // Implement edit album logic
                        println("Edit")
                    }
                    R.id.action_delete_album -> {
                        // Implement delete album logic
                        println("Delete")
                    }
                    R.id.action_add_to_queue -> {
                        // Implement add to queue logic
                        println("Queue")
                    }
                    R.id.action_share_album -> {
                        // Implement share album logic
                        println("Share")
                    }
                    else -> false
                }
                true
            }
            popup.show()
            */

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
            tracksAdapter = AlbumTracksAdapter(trackList, dbHelper) { track -> openTrack(track) }
            recyclerViewTracks.adapter = tracksAdapter
        } else {
            Toast.makeText(requireContext(), "Album not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openTrack(track: Track) {
        val fragment = SingleTrackFragment.newInstance(track)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        Toast.makeText(requireContext(), track.name, Toast.LENGTH_SHORT).show()
    }
}
