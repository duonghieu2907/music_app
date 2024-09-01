package com.example.mymusicapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.mymusicapp.bottomnavigation.ExploreFragment
import com.example.mymusicapp.bottomnavigation.HomeFragment
import com.example.mymusicapp.bottomnavigation.LibraryFragment
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.PlaylistTrack
import com.example.mymusicapp.models.Track
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //load DB

        val dbHelper = MusicAppDatabaseHelper(this)
        addDummyDataToDatabase(dbHelper)



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
    fun addDummyDataToDatabase(dbHelper: MusicAppDatabaseHelper) {
        // Add dummy user, artist, album, playlist, and track data for testing

        // Add dummy playlist
        val playlistId = dbHelper.addPlaylist(Playlist("1", "1", "Lofi Loft", "https://via.placeholder.com/150"))

        // Add dummy tracks
        val albumId = dbHelper.addAlbum(Album("1", "1", "Chill Vibes", "2024-01-01", "https://via.placeholder.com/150"))
        dbHelper.addTrack(Track("1", albumId.toString(), "grainy days", "2:43", "path/to/audio"))
        dbHelper.addTrack(Track("2", albumId.toString(), "Coffee", "3:15", "path/to/audio"))

        // Link tracks to playlist
        dbHelper.addPlaylistTrack(PlaylistTrack(playlistId.toString(), "1", 1))
        dbHelper.addPlaylistTrack(PlaylistTrack(playlistId.toString(), "2", 2))
    }
}
