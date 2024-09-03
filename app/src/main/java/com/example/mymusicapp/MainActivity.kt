package com.example.mymusicapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.mymusicapp.bottomnavigation.ExploreFragment
import com.example.mymusicapp.bottomnavigation.HomeFragment
import com.example.mymusicapp.bottomnavigation.LibraryFragment
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var dbHelper: MusicAppDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //load DB

        dbHelper = MusicAppDatabaseHelper(this)
        addDummyDataToDatabase()


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_explore -> {
                    loadFragment(ExploreFragment())
                    true
                }
                R.id.nav_library -> {
                    loadFragment(LibraryFragment())
                    true
                }
                else -> false
            }
        }

        // Load the default fragment
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.nav_home
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    private fun addDummyDataToDatabase() {
        try {
            // Add dummy users
            dbHelper.addUser(User("1", "Alice", "alice@example.com", "password123", "1990-01-01", "https://via.placeholder.com/150"))
            dbHelper.addUser(User("2", "Bob", "bob@example.com", "securepass", "1985-05-10", "https://via.placeholder.com/150"))
            Log.d("MainActivity", "Dummy users added")

            // Add dummy artists
            val artistId1 = dbHelper.addArtist(Artist("1", "Chill Artist", "Lofi", "https://via.placeholder.com/150"))
            val artistId2 = dbHelper.addArtist(Artist("2", "Jazz Master", "Jazz", "https://via.placeholder.com/150"))
            Log.d("MainActivity", "Dummy artists added")

            // Add dummy albums
            val albumId1 = dbHelper.addAlbum(Album("1", artistId1, "Chill Vibes", "2024-01-01", "https://via.placeholder.com/150"))
            val albumId2 = dbHelper.addAlbum(Album("2", artistId2, "Smooth Jazz", "2024-05-01", "https://via.placeholder.com/150"))
            Log.d("MainActivity", "Dummy albums " + albumId1.toString() + " added")

            // Add dummy tracks
            dbHelper.addTrack(Track("1", albumId1, "grainy days", "2:43", "path/to/audio1"))
            dbHelper.addTrack(Track("2", albumId1, "Coffee", "3:15", "path/to/audio2"))
            dbHelper.addTrack(Track("3", albumId2, "Jazz in the Night", "4:20", "path/to/audio3"))
            dbHelper.addTrack(Track("4", albumId2, "Smooth Flow", "3:50", "path/to/audio4"))
            Log.d("MainActivity", "Dummy tracks added")

            // Add dummy playlist
            val playlistId = dbHelper.addPlaylist(Playlist("1", "1", "Lofi Loft", "https://via.placeholder.com/150"))
            Log.d("MainActivity", "Dummy playlist " + playlistId.toString() + " added")

            // Link tracks to playlist
            dbHelper.addPlaylistTrack(PlaylistTrack(playlistId.toString(), "1", 1))
            dbHelper.addPlaylistTrack(PlaylistTrack(playlistId.toString(), "2", 2))
            dbHelper.addPlaylistTrack(PlaylistTrack(playlistId.toString(), "3", 3))
            dbHelper.addPlaylistTrack(PlaylistTrack(playlistId.toString(), "4", 4))
            Log.d("MainActivity", "Tracks linked to playlist")

        } catch (e: Exception) {
            Log.e("MainActivity", "Error adding dummy data: ${e.message}")
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //dbHelper.close()
    }


}
