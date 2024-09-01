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

    private var playlistId: Int = 0  // Hold the playlistId to fetch data

    companion object {
        private const val ARG_PLAYLIST_ID = "playlist_id"

        // Factory method to create a new instance of PlaylistFragment
        fun newInstance(playlistId: Int): PlaylistFragment {
            val fragment = PlaylistFragment()
            val args = Bundle()
            args.putInt(ARG_PLAYLIST_ID, playlistId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the playlistId from the arguments
        arguments?.let {
            playlistId = it.getInt(ARG_PLAYLIST_ID)
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
        val playlist: Playlist? = dbHelper.getPlaylist(playlistId)

        if (playlist != null) {
            playlistTitle.text = playlist.name
            playlistSubtitle.text = "Your personalized playlist"

            // Fetch tracks for this playlist
            val trackList: List<Track> = dbHelper.getTracksByPlaylistId(playlistId)
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

        /*

        val playlist = dbHelper.getPlaylist(playlistId) // Get the playlist from the database
        val tracks = dbHelper.getTracksByPlaylistId(playlistId) // Get the tracks of the playlist

        if (playlist != null) {
            // Set playlist details
            playlistTitle.text = playlist.name
            playlistSubtitle.text = "soft, chill, dreamy, lo-fi beats" // Static subtitle for now, update as needed

            // Set up RecyclerView with tracks fetched from the database
            recyclerViewSongs.layoutManager = LinearLayoutManager(requireContext())
            songsAdapter = SongsAdapter(tracks) { track -> openTrack(track) }
            recyclerViewSongs.adapter = songsAdapter
        } else {
            Toast.makeText(requireContext(), "Playlist not found!", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun openTrack(track: Track) {
        // Open SingleTrackFragment (assuming a similar fragment exists for tracks)
        val fragment = SingleSongFragment.newInstance(track)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        Toast.makeText(requireContext(), track.name, Toast.LENGTH_SHORT).show()
    }

    // Example function to add dummy data to the database


}

*/