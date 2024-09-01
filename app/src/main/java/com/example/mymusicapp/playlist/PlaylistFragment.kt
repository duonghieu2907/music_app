package com.example.mymusicapp.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import android.widget.Toast
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.*

class PlaylistFragment : Fragment() {

    private lateinit var playlistTitle: TextView
    private lateinit var playlistSubtitle: TextView
    private lateinit var recyclerViewTracks: RecyclerView
    private lateinit var tracksAdapter: PlaylistTracksAdapter
    private lateinit var dbHelper: MusicAppDatabaseHelper

    private var playlistId: String = ""  // Changed to String

    companion object {
        private const val ARG_PLAYLIST_ID = "playlist_id"

        // Factory method to create a new instance of PlaylistFragment
        fun newInstance(playlistId: String): PlaylistFragment {  // Changed parameter to String
            val fragment = PlaylistFragment()
            val args = Bundle()
            args.putString(ARG_PLAYLIST_ID, playlistId)  // Changed to putString
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the playlistId from the arguments
        arguments?.let {
            playlistId = it.getString(ARG_PLAYLIST_ID) ?: ""  // Changed to getString
        }

        // Initialize the database helper
        dbHelper = MusicAppDatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_single_playlist, container, false)

        // Initialize UI components
        playlistTitle = view.findViewById(R.id.playlistTitle)
        playlistSubtitle = view.findViewById(R.id.playlistSubtitle)
        recyclerViewTracks = view.findViewById(R.id.recyclerViewSongs)

        // Set up RecyclerView
        recyclerViewTracks.layoutManager = LinearLayoutManager(requireContext())

        // Load data from the database
        loadPlaylistData()

        return view
    }

    private fun loadPlaylistData() {
        // Fetch playlist details using playlistId
        val playlist: Playlist? = dbHelper.getPlaylist(playlistId)  // Ensure dbHelper uses String type

        if (playlist != null) {
            playlistTitle.text = playlist.name
            playlistSubtitle.text = "Your personalized playlist"

            // Fetch tracks for this playlist
            val trackList: List<Track> = dbHelper.getTracksByPlaylistId(playlistId)  // Ensure this method accepts String
            tracksAdapter = PlaylistTracksAdapter(trackList, dbHelper) { track -> openTrack(track) }
            recyclerViewTracks.adapter = tracksAdapter
        } else {
            Toast.makeText(requireContext(), "Playlist not found", Toast.LENGTH_SHORT).show()
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
