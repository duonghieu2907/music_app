package com.example.mymusicapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.adamratzman.spotify.models.SpotifyImage
import com.example.mymusicapp.bottomnavigation.ExploreFragment
import com.example.mymusicapp.bottomnavigation.HomeFragment
import com.example.mymusicapp.bottomnavigation.LibraryFragment
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.data.SpotifyData
import com.example.mymusicapp.models.Artist
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.PlaylistTrack
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var dbHelper: MusicAppDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //load DB

        dbHelper = MusicAppDatabaseHelper(this)
        CoroutineScope(Dispatchers.Main).launch {
            addDummyDataToDatabase()
        }

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

    private suspend fun addDummyDataToDatabase() {
        try {
            // Add dummy users
            val spotifyData = SpotifyData()
            spotifyData.buildSearchApi()

            var userRaw = spotifyData.getUserOnline("Salmoe")
            dbHelper.addUser(User(userRaw!!.id, userRaw.displayName!!, "", "", "1990-01-01", checkImageReturnUrl(userRaw.images)))

            userRaw = spotifyData.getUserOnline("duonghieu")
            dbHelper.addUser(User(userRaw!!.id, userRaw.displayName!!, "", "", "1985-05-10", checkImageReturnUrl(userRaw.images)))

            //dbHelper.addUser(User("1", "Alice", "alice@example.com", "password123", "1990-01-01", "https://via.placeholder.com/150"))
            //dbHelper.addUser(User("2", "Bob", "bob@example.com", "securepass", "1985-05-10", "https://via.placeholder.com/150"))
            Log.d("MainActivity", "Users added")

            // Add dummy artists
            var artistRaw = spotifyData.findArtist("06HL4z0CvFAxyc27GXpf02") //Taylor Swift
            val artistId1 = dbHelper.addArtist(Artist(artistRaw!!.id, artistRaw.name, artistRaw.genres[0], checkImageReturnUrl(artistRaw.images)))

            artistRaw = spotifyData.findArtist("5M3ffmRiOX9Q8Y4jNeR5wu") //Wren Evans
            val artistId2 = dbHelper.addArtist(Artist(artistRaw!!.id, artistRaw.name, artistRaw.genres[0], checkImageReturnUrl(artistRaw.images)))
            Log.d("MainActivity", "Artists added")

            // Add dummy albums
            var albumsRaw = spotifyData.findAlbumsFromArtist("06HL4z0CvFAxyc27GXpf02")!!

            for (i in 0..< albumsRaw.size) {
                dbHelper.addAlbum(albumsRaw[i])
            }

            albumsRaw = spotifyData.findAlbumsFromArtist("5M3ffmRiOX9Q8Y4jNeR5wu")!!
            for (i in 0..< albumsRaw.size) {
                dbHelper.addAlbum(albumsRaw[i])
            }

            Log.d("MainActivity", "Albums added")

            // Add dummy tracks
            val allAlbumId = dbHelper.getAllAlbumId()!!
            for (i in 0..<allAlbumId.size) {
                val tracksRaw = spotifyData.findTracksFromAlbum(allAlbumId[i])!!

                for(k in 0..<tracksRaw.size) dbHelper.addTrack(Track(tracksRaw[k].trackId, allAlbumId[i],
                    tracksRaw[k].name, tracksRaw[k].duration, tracksRaw[k].path))
            }

            //dbHelper.addTrack(Track("1", albumId1.toString(), "grainy days", "2:43", "path/to/audio1"))
            //dbHelper.addTrack(Track("2", albumId1.toString(), "Coffee", "3:15", "path/to/audio2"))
            //dbHelper.addTrack(Track("3", albumId2.toString(), "Jazz in the Night", "4:20", "path/to/audio3"))
            //dbHelper.addTrack(Track("4", albumId2.toString(), "Smooth Flow", "3:50", "path/to/audio4"))
            Log.d("MainActivity", "Tracks added")

            // Add dummy playlist
            val playlistId = dbHelper.addPlaylist(Playlist("1", "1", "Lofi Loft", "https://via.placeholder.com/150"))
            Log.d("MainActivity", "Dummy playlist " + playlistId.toString() + " added")

            // Link tracks to playlist
            val tracksRaw = spotifyData.findTracksFromAlbum(allAlbumId[0])!!
            for(i in 0..<tracksRaw.size) {
                dbHelper.addPlaylistTrack(PlaylistTrack(playlistId.toString(), tracksRaw[i].trackId, i+1))
            }
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

    private fun checkImageReturnUrl(sample : List<SpotifyImage>) : String {
        return when {
            sample.isEmpty() -> ""
            else -> sample.firstOrNull()!!.url
        }
    }
}
