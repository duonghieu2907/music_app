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
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.playlist.PlaylistFragment

class ExploreFragment : Fragment() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var allGenres: List<String>
    private lateinit var allGenres2: List<String>
    private lateinit var dbHelper: MusicAppDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        // Show the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.showBottomNavigation()
        }

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
            // Create an instance of the SearchFragment
            val searchFragment = SearchFragment()

            // Use FragmentTransaction to replace the current fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, searchFragment) // Replace with your container ID
                .addToBackStack(null)  // Optional: adds to the back stack
                .commit()
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve and print all genres
        allGenres = dbHelper.getAllGenres()

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

        // Set click listeners for browse boxes
        boxBrowse1.setOnClickListener { onBrowseClick(1) }
        boxBrowse2.setOnClickListener { onBrowseClick(2) }
        boxBrowse3.setOnClickListener { onBrowseClick(3) }
        boxBrowse4.setOnClickListener { onBrowseClick(4) }
        boxBrowse5.setOnClickListener { onBrowseClick(5) }
        boxBrowse6.setOnClickListener { onBrowseClick(6) }

        // Handle click for drawer items
        handleDrawerNavigation(drawerLayout)

        // Update genre names dynamically
        updateGenreNames(view)
    }

    // Function to update genre names dynamically
    private fun updateGenreNames(view: View) {
        // Filter the genres list to exclude "Unknown" entries
        val allGenres = allGenres.filter { it != "Unknown" }

        // Find each genre name TextView by its ID
        val nameGenre1: TextView = view.findViewById(R.id.genre_name_1)
        val nameGenre2: TextView = view.findViewById(R.id.genre_name_2)

        // Update the genre names if they exist
        if (allGenres.isNotEmpty()) {
            nameGenre1.text = allGenres.getOrNull(0) ?: "Unknown Genre"
            nameGenre2.text = allGenres.getOrNull(1) ?: "Unknown Genre"
        }

        // Find each browse name TextView by its ID
        val nameBrowse1: TextView = view.findViewById(R.id.browse_name_1)
        val nameBrowse2: TextView = view.findViewById(R.id.browse_name_2)
        val nameBrowse3: TextView = view.findViewById(R.id.browse_name_3)
        val nameBrowse4: TextView = view.findViewById(R.id.browse_name_4)
        val nameBrowse5: TextView = view.findViewById(R.id.browse_name_5)
        val nameBrowse6: TextView = view.findViewById(R.id.browse_name_6)

        // Shuffle the filtered list to get random entries
        allGenres2 = allGenres.shuffled()

        // Update the browse names with random genres, or default to "Unknown Browse"
        nameBrowse1.text = allGenres2.getOrNull(0) ?: "Unknown Browse"
        nameBrowse2.text = allGenres2.getOrNull(1) ?: "Unknown Browse"
        nameBrowse3.text = allGenres2.getOrNull(2) ?: "Unknown Browse"
        nameBrowse4.text = allGenres2.getOrNull(3) ?: "Unknown Browse"
        nameBrowse5.text = allGenres2.getOrNull(4) ?: "Unknown Browse"
        nameBrowse6.text = allGenres2.getOrNull(5) ?: "Unknown Browse"
    }


    private fun handleDrawerNavigation(view: View) {
        val settingsButton: TextView = view.findViewById(R.id.settings_button)
        val historyButton: TextView = view.findViewById(R.id.history_button)
        val queueButton: TextView = view.findViewById(R.id.queue_button)

        settingsButton.setOnClickListener {
            // Navigate to Settings Fragment
            navigateToSettings()
        }

        historyButton.setOnClickListener {
            // Navigate to History Fragment
            navigateToHistory()
        }

        queueButton.setOnClickListener {
            // Navigate to Queue Fragment
            navigateToQueue()
        }
    }

    private fun navigateToSettings() {
        Log.d("ExploreFragment", "Navigating to SettingsFragment.")
        // Replace with your SettingsFragment or Activity
//        val settingsFragment = SettingsFragment()
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, settingsFragment)  // Replace with your fragment container ID
//            .addToBackStack(null)  // Optional: Add this transaction to the back stack
//            .commit()
//        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun navigateToHistory() {
        Log.d("ExploreFragment", "Navigating to HistoryFragment.")
        // Replace with your HistoryFragment or Activity
//        val historyFragment = HistoryFragment()
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, historyFragment)  // Replace with your fragment container ID
//            .addToBackStack(null)  // Optional: Add this transaction to the back stack
//            .commit()
//        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun navigateToQueue() {
        Log.d("ExploreFragment", "Navigating to QueueFragment.")
        // Navigate to QueueFragment
//        val queueFragment = QueueFragment()
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, queueFragment)  // Replace with your fragment container ID
//            .addToBackStack(null)  // Optional: Add this transaction to the back stack
//            .commit()
//        drawerLayout.closeDrawer(GravityCompat.START)
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
        if (browseId <= allGenres2.size) {
            val genre = allGenres2[browseId - 1]
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
