package com.example.mymusicapp.bottomnavigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.playlist.PlaylistFragment

class ExploreFragment : Fragment() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var allGenres: List<String>
    private lateinit var dbHelper: MusicAppDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        // Reference to drawer layout
        drawerLayout = requireActivity().findViewById(R.id.main)

        // Initialize Database Helper
        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Avatar icon
        val avatarIcon = view.findViewById<ImageView>(R.id.avatar_icon)
        avatarIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val searchButton: TextView = view.findViewById(R.id.search_button)

        searchButton.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve and print all genres
        allGenres = dbHelper.getAllGenres()

        // Log the genres to verify
        Log.d("ExploreFragment", "All Genres: $allGenres")

        // Automatically add playlists for each genre if they don't already exist
        allGenres.forEach { genre ->
            val playlistName = "$genre playlist"
            dbHelper.addGenrePlaylistIfNotExists(playlistName, genre, "1")
        }

        // Find each box by its ID
        val boxGenre1: LinearLayout = view.findViewById(R.id.box_genre_1)
        val boxGenre2: LinearLayout = view.findViewById(R.id.box_genre_2)
        val boxBrowse1: LinearLayout = view.findViewById(R.id.box_browse_1)
        val boxBrowse2: LinearLayout = view.findViewById(R.id.box_browse_2)
        val boxBrowse3: LinearLayout = view.findViewById(R.id.box_browse_3)
        val boxBrowse4: LinearLayout = view.findViewById(R.id.box_browse_4)
        val boxBrowse5: LinearLayout = view.findViewById(R.id.box_browse_5)
        val boxBrowse6: LinearLayout = view.findViewById(R.id.box_browse_6)

        // Set click listeners for genre boxes
        boxGenre1.setOnClickListener { onTopGenreClick(1) }
        boxGenre2.setOnClickListener { onTopGenreClick(2) }

        // Set click listeners for browse boxes (optional, based on your design)
        boxBrowse1.setOnClickListener { onBrowseClick(1) }
        boxBrowse2.setOnClickListener { onBrowseClick(2) }
        boxBrowse3.setOnClickListener { onBrowseClick(3) }
        boxBrowse4.setOnClickListener { onBrowseClick(4) }
        boxBrowse5.setOnClickListener { onBrowseClick(5) }
        boxBrowse6.setOnClickListener { onBrowseClick(6) }
    }

    // Function to create a playlist for a given genre
    private fun onTopGenreClick(genreId: Int) {
        if (genreId <= allGenres.size) {
            val genre = allGenres[genreId - 1]
            val playlistID = dbHelper.getPlaylistIdByName("$genre playlist", "1")
            val playlistFragment =
                playlistID?.let { PlaylistFragment.newInstance(it) } //Transfer id
            if (playlistFragment != null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, playlistFragment)  // Replace with your fragment container ID
                    .addToBackStack(null)  // Optional: Add this transaction to the back stack
                    .commit()
            }

            Toast.makeText(requireContext(), "Clicked on Playlist: $genre playlist", Toast.LENGTH_SHORT).show()
        }
        println("Browse box $genreId clicked")
    }

    // Browse click handling function
    private fun onBrowseClick(browseId: Int) {
        // Handle browse click
        // You can use browseId to differentiate between different browse items
        // Example: start a new fragment or activity
        if (browseId <= allGenres.size) {
            val genre = allGenres[browseId - 1]
            val playlistID = dbHelper.getPlaylistIdByName("$genre playlist", "1")
            val playlistFragment =
                playlistID?.let { PlaylistFragment.newInstance(it) } //Transfer id
            if (playlistFragment != null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, playlistFragment)  // Replace with your fragment container ID
                    .addToBackStack(null)  // Optional: Add this transaction to the back stack
                    .commit()
            }

            Toast.makeText(requireContext(), "Clicked on Playlist: $genre playlist", Toast.LENGTH_SHORT).show()
        }
        println("Browse box $browseId clicked")
    }

}
