package com.example.mymusicapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.adamratzman.spotify.models.SpotifyImage
import com.example.mymusicapp.bottomnavigation.ExploreFragment
import com.example.mymusicapp.bottomnavigation.HomeFragment
import com.example.mymusicapp.bottomnavigation.LibraryFragment
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.data.SpotifyData
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Artist
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.PlaylistTrack
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var dbHelper: MusicAppDatabaseHelper
    private var authCode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //load DB
        try {
            dbHelper = MusicAppDatabaseHelper(this)
            //dbHelper.deleteAll() //Run this line to delete everything

            //Run this to update or insert data to database
            lifecycleScope.launch(Dispatchers.IO) {
                val spotifyData = SpotifyData()
                //spotifyData.buildAuthcode(activity = this@MainActivity)

                /*while (authCode == null) {
                    delay(1000) //wait until receive authCode
                }*/

                //authCode received, built clientApi and AppApi
                //spotifyData.buildClientApi(authCode!!)
                //spotifyData.buildAppApi()
                //dbHelper.deleteAll() //Run this line to delete all data
                //To change database, wipe all data in emulator

                //Add dummy data
                //dbHelper.exportDatabaseToFile()
                //addDummyDataToDatabase(spotifyData)


                //dbHelper.addUser(User("1", "Official", "", "", "", ""))
                //Log.d("mainActivity", "Official added")

                //dummy to work with playlist
                dbHelper.addUser(User("3", "Test", "", "", "", ""))
                insertDummyData()

            }
        } catch (e : Exception) {
            Log.e("mainActivity", "Error during background: $e")
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

        dbHelper.exportDatabaseToFile()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private suspend fun addDummyDataToDatabase(spotifyData: SpotifyData) {
        try {
            // Add dummy users
            var userRaw = spotifyData.getUserOnline("31j3r46xev5snbqwjqbuwbrm6cwu")
            dbHelper.addUser(
                User(
                    userRaw!!.id,
                    userRaw.displayName!!,
                    "",
                    "",
                    "1990-01-01",
                    checkImageReturnUrl(userRaw.images)
                )
            )

            userRaw = spotifyData.getUserOnline("duonghieu")
            dbHelper.addUser(
                User(
                    userRaw!!.id,
                    userRaw.displayName!!,
                    "",
                    "",
                    "1985-05-10",
                    checkImageReturnUrl(userRaw.images)
                )
            )

            Log.d("MainActivity", "Users added")

            //Playlist
            val playlistRaw = spotifyData.findPlaylistsFromUser("31j3r46xev5snbqwjqbuwbrm6cwu")
            val playlistLength = playlistRaw!!.size
            for (i in 0..<playlistLength) {
                dbHelper.addPlaylist(
                    Playlist(
                        playlistRaw[i].playlistId,
                        playlistRaw[i].userId,
                        playlistRaw[i].name,
                        playlistRaw[i].image
                    )
                )
            }
            Log.d("mainActivity", "All playlist Added")

            //Tracks
            for(i in 0..<playlistLength) {
                val trackRaw = spotifyData.findTracksFromPlaylist(playlistRaw[i].playlistId)
                val trackRawLength = trackRaw!!.size
                for(k in 0..<trackRawLength) {
                    //Add track
                    dbHelper.addTrack(
                        Track(
                            trackRaw[k].trackId,
                            trackRaw[k].albumId,
                            trackRaw[k].name,
                            trackRaw[k].duration,
                            trackRaw[k].path
                        )
                    )

                    dbHelper.addPlaylistTrack(
                        PlaylistTrack(
                            playlistRaw[i].playlistId,
                            trackRaw[k].trackId,
                            k+1
                        )
                    )

                    //Add albums
                    val albumRaw = spotifyData.findAlbumFromTracks(trackRaw[k].albumId)
                    dbHelper.addAlbum(
                        Album(
                            albumRaw!!.id,
                            albumRaw.artists[0].id,
                            albumRaw.name,
                            albumRaw.releaseDate.toString(),
                            checkImageReturnUrl(albumRaw.images)
                        )
                    )

                    //Add artists
                    val artistRaw = spotifyData.findArtist(albumRaw.artists[0].id)
                    dbHelper.addArtist(
                        Artist(
                            artistRaw!!.id,
                            artistRaw.name,
                            artistRaw.genres.firstOrNull()?:"Unknown",
                            checkImageReturnUrl(artistRaw.images)
                        )
                    )
                }
                Log.d("mainActivity", "track, album, artist $i")
            }

            // Add dummy tracks
            val allAlbumId = dbHelper.getAllAlbumId()!!
            for (i in 0..<allAlbumId.size) {
                val tracksRaw = spotifyData.findTracksFromAlbum(allAlbumId[i])!!

                for (k in 0..<tracksRaw.size) dbHelper.addTrack(
                    Track(
                        tracksRaw[k].trackId, allAlbumId[i],
                        tracksRaw[k].name, tracksRaw[k].duration, tracksRaw[k].path
                    )
                )
            }
            Log.d("MainActivity", "Tracks added")

        } catch (e: Exception) {
            Log.e("MainActivity", "Error adding dummy data: ${e.message}")
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1001 && resultCode == RESULT_OK) {
            authCode = data?.getStringExtra("AUTH_CODE")
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    private fun checkImageReturnUrl(sample: List<SpotifyImage>): String {
        return when {
            sample.isEmpty() -> ""
            else -> sample.firstOrNull()!!.url
        }
    }


    fun hideBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE
    }

    fun showBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.visibility = View.VISIBLE
    }

    // Sample Tracks
    val dummyTracks = listOf(
        Track(
            trackId = "track1",
            albumId = "album1",
            name = "Song One",
            duration = "03:45",
            path = "https://example.com/track1.mp3"
        ),
        Track(
            trackId = "track2",
            albumId = "album1",
            name = "Song Two",
            duration = "04:20",
            path = "https://example.com/track2.mp3"
        ),
        Track(
            trackId = "track3",
            albumId = "album2",
            name = "Song Three",
            duration = "05:10",
            path = "https://example.com/track3.mp3"
        )
    )

    // Sample Playlists
    val dummyPlaylists = listOf(
        Playlist(
            playlistId = "playlist1",
            userId = "3",
            name = "My Favorite Songs",
            image = "https://example.com/playlist1.jpg"
        ),
        Playlist(
            playlistId = "playlist2",
            userId = "3",
            name = "Chill Vibes",
            image = "https://example.com/playlist2.jpg"
        )
    )
    private fun insertDummyData() {
        // Insert dummy tracks
        dummyTracks.forEach { track ->
            dbHelper.addTrack(track)  // Assuming you have a function to add tracks
        }

        // Insert dummy playlists
        dummyPlaylists.forEach { playlist ->
            dbHelper.addPlaylist(playlist)  // Assuming you have a function to add playlists
        }

        // Optionally, associate tracks with playlists
        dbHelper.addPlaylistTrack("playlist1","track1")
        dbHelper.addPlaylistTrack("playlist1","track2")
        dbHelper.addPlaylistTrack("playlist2","track3")

        Toast.makeText(this,  "Dummy data inserted for testing", Toast.LENGTH_SHORT).show()
    }




}
