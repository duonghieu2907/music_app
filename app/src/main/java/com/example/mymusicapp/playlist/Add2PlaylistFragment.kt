package com.example.mymusicapp.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.Track

class Add2PlaylistFragment : Fragment() {

    private lateinit var playlistRecyclerView: RecyclerView
    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var backButton: ImageView
    private lateinit var newPlaylistButton: AppCompatButton
    private lateinit var track: Track  // Track data to be passed
    private lateinit var dbHelper: MusicAppDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_add_2_playlist, container, false)

        // Hide the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }

        // Retrieve the Track object passed as argument
        track = arguments?.getParcelable("TRACK") ?: throw IllegalStateException("Track not passed to Add2PlaylistFragment")

        backButton = view.findViewById(R.id.back)

        // Set button click handlers
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        newPlaylistButton = view.findViewById(R.id.newPlaylistButton)

        // Set button click handlers
        newPlaylistButton.setOnClickListener {


            openFragment(FragmentNewPlaylist.newInstance(track.trackId))  // Pass the track data when opening Add2PlaylistFragment



        }

        // Initialize RecyclerView
        playlistRecyclerView = view.findViewById(R.id.playlistRecyclerView)
        playlistRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        dbHelper = MusicAppDatabaseHelper(requireContext())


        // Create an adapter and set it to the RecyclerView
        val playlistIds = dbHelper.getAllPlaylistId()  // Get the list of all playlist IDs

        val app = requireActivity().application as Global


        val playlists = mutableListOf<Playlist>()      // Create a mutable list to store Playlist objects

        // Loop through each playlist ID and get the corresponding Playlist object
        if (playlistIds != null) {
            for (id in playlistIds) {
                val playlist = dbHelper.getPlaylist(id)    // Fetch the Playlist object using the ID
                if (playlist != null && playlist.userId == app.curUserId) {
                    playlists.add(playlist)                // Add the Playlist object to the list
                }
            }
        }

        // Set up the adapter
        playlistAdapter = PlaylistAdapter(playlists, dbHelper)
        playlistRecyclerView.adapter = playlistAdapter

        // Handle playlist item click
        playlistAdapter.onPlaylistClick = { playlist ->

            // Check if the track already exists in the playlist
            val playlistTracks = dbHelper.getTracksByPlaylistId(playlist.playlistId)


            // Check if the track already exists in the playlist
            val trackExists = playlistTracks.any { it.trackId == track.trackId }

            if (!trackExists) {
                // Add the track if it doesn't exist
                dbHelper.addPlaylistTrack(playlist.playlistId, track.trackId)

                // Update the playlist object to reflect the new track count
                val updatedPlaylist = dbHelper.getPlaylist(playlist.playlistId)

                if (updatedPlaylist != null) {
                    val position = playlists.indexOf(playlist)
                    playlists[position] = updatedPlaylist // Update the playlist in the list

                    // Notify the adapter about the updated playlist
                    playlistAdapter.notifyItemChanged(position)

                    // You can show a message to the user
                    Toast.makeText(requireContext(), "Track added to ${playlist.name}", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show a message to the user if the track already exists
                Toast.makeText(requireContext(), "Track has already existed in ${playlist.name}", Toast.LENGTH_SHORT).show()
            }
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

    override fun onDestroyView() {
        super.onDestroyView()

        // Show the navigation bar when this fragment is destroyed
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.showBottomNavigation()
        }
    }


    override fun onResume() {
        super.onResume()
        // Hide the bottom navigation bar when this fragment is resumed
        // Hide the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }
    }

    // Helper function to open fragments
    private fun openFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment) // Replace with the ID of your container
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
