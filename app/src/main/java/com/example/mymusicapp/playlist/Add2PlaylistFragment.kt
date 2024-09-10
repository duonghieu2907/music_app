package com.example.mymusicapp.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.Track

class Add2PlaylistFragment : Fragment() {

    private lateinit var playlistRecyclerView: RecyclerView
    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var backButton: ImageView
    private lateinit var track: Track  // Track data to be passed
    private lateinit var dbHelper: MusicAppDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_add_2_playlist, container, false)

        // Retrieve the Track object passed as argument
        track = arguments?.getParcelable("TRACK") ?: throw IllegalStateException("Track not passed to Add2PlaylistFragment")

        backButton = view.findViewById(R.id.back)

        // Set button click handlers
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Initialize RecyclerView
        playlistRecyclerView = view.findViewById(R.id.playlistRecyclerView)
        playlistRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        dbHelper = MusicAppDatabaseHelper(requireContext())


        // Create an adapter and set it to the RecyclerView
        val playlistIds = dbHelper.getAllPlaylistId()  // Get the list of all playlist IDs
        val playlists = mutableListOf<Playlist>()      // Create a mutable list to store Playlist objects

        // Loop through each playlist ID and get the corresponding Playlist object
        if (playlistIds != null) {
            for (id in playlistIds) {
                val playlist = dbHelper.getPlaylist(id)    // Fetch the Playlist object using the ID
                if (playlist != null) {
                    playlists.add(playlist)                // Add the Playlist object to the list
                }
            }
        }

        // Set up the adapter
        playlistAdapter = PlaylistAdapter(playlists, dbHelper)
        playlistRecyclerView.adapter = playlistAdapter

        // Handle playlist item click
        playlistAdapter.onPlaylistClick = { playlist ->

                dbHelper.addPlaylistTrack(playlist.playlistId, track.trackId)
                // You can show a message to the user
                Toast.makeText(requireContext(), "Track added to ${playlist.name}", Toast.LENGTH_SHORT).show()

        }

        return view
    }

    companion object {
        fun newInstance(track: Track): Add2PlaylistFragment {
            val fragment = Add2PlaylistFragment()
            val args = Bundle()
            args.putParcelable("TRACK", track)  // Pass the Track object to this fragment
            fragment.arguments = args
            return fragment
        }
    }
}
