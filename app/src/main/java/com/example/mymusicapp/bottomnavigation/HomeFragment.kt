package com.example.mymusicapp.bottomnavigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.LoginActivity
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.album.AlbumFragment
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.data.PlayerViewModel
import com.example.mymusicapp.data.PlayerViewModelFactory
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.TrackQueue
import com.example.mymusicapp.playlist.SingleTrackFragment
import com.example.mymusicapp.queue.QueueFragment
import com.google.android.exoplayer2.ExoPlayer

class HomeFragment : Fragment() {
    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var curUser: String
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sharedPref: SharedPreferences
    private lateinit var context: Context
    private lateinit var activity: Activity

    private lateinit var boxRecent1: LinearLayout
    private lateinit var boxRecent2: LinearLayout
    private lateinit var boxRecent3: LinearLayout
    private lateinit var boxRecent4: LinearLayout
    private lateinit var boxRecent5: LinearLayout
    private lateinit var boxRecent6: LinearLayout

    private lateinit var albumCover1: ImageView
    private lateinit var albumCover2: ImageView
    private lateinit var albumCover3: ImageView
    private lateinit var albumCover4: ImageView
    private lateinit var albumCover5: ImageView
    private lateinit var albumCover6: ImageView

    private lateinit var albumName1: TextView
    private lateinit var albumName2: TextView
    private lateinit var albumName3: TextView
    private lateinit var albumName4: TextView
    private lateinit var albumName5: TextView
    private lateinit var albumName6: TextView

    //player view model and exoPlayer
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var exoPlayer: ExoPlayer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //Init sharedPref and context
        context = requireContext()
        activity = requireActivity()
        sharedPref = activity.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        // Reference to drawer layout
        drawerLayout = requireActivity().findViewById(R.id.main)

        // Avatar icon
        val avatarIcon = view.findViewById<ImageView>(R.id.avatar_icon)
        avatarIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Handle click for drawer items
        handleDrawerNavigation(drawerLayout)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the database helper
        dbHelper = MusicAppDatabaseHelper(context)

        val application = requireActivity().application as Global
        curUser = application.curUserId!!

        //Init exoPlayer
        exoPlayer = ExoPlayer.Builder(context).build()
        playerViewModel = ViewModelProvider(activity as FragmentActivity,
            PlayerViewModelFactory(exoPlayer, dbHelper, curUser))[PlayerViewModel::class.java]
        playerViewModel.exoPlayer.let {
            exoPlayer = it
        }

        val recentlyPlayedAlbums = dbHelper.getNewest("album", curUser) as ArrayList<*>
        val albums = (recentlyPlayedAlbums as? List<*>)?.mapNotNull { it as? Album } ?: arrayListOf()

        // Initialize album boxes (LinearLayouts)
        boxRecent1 = view.findViewById(R.id.box_recent_1)
        boxRecent2 = view.findViewById(R.id.box_recent_2)
        boxRecent3 = view.findViewById(R.id.box_recent_3)
        boxRecent4 = view.findViewById(R.id.box_recent_4)
        boxRecent5 = view.findViewById(R.id.box_recent_5)
        boxRecent6 = view.findViewById(R.id.box_recent_6)

        // Initialize album cover ImageViews
        albumCover1 = view.findViewById(R.id.album_cover_1)
        albumCover2 = view.findViewById(R.id.album_cover_2)
        albumCover3 = view.findViewById(R.id.album_cover_3)
        albumCover4 = view.findViewById(R.id.album_cover_4)
        albumCover5 = view.findViewById(R.id.album_cover_5)
        albumCover6 = view.findViewById(R.id.album_cover_6)

        // Initialize album name TextViews
        albumName1 = view.findViewById(R.id.album_name_1)
        albumName2 = view.findViewById(R.id.album_name_2)
        albumName3 = view.findViewById(R.id.album_name_3)
        albumName4 = view.findViewById(R.id.album_name_4)
        albumName5 = view.findViewById(R.id.album_name_5)
        albumName6 = view.findViewById(R.id.album_name_6)

        updateRecentlyPlayedAlbums(albums)

        setupRecyclerView(view)

        view.findViewById<LinearLayout>(R.id.mix_item_1).setOnClickListener {
            val boxColor = (it.findViewById<LinearLayout>(R.id.mix_item_1).background as? ColorDrawable)?.color ?: Color.TRANSPARENT
            val underlineColor = (it.findViewById<View>(R.id.underline).background as? ColorDrawable)?.color ?: Color.TRANSPARENT

            openMixPlaylist("Mix 1", boxColor, underlineColor)
        }

        view.findViewById<LinearLayout>(R.id.mix_item_2).setOnClickListener {
            val boxColor = (it.findViewById<LinearLayout>(R.id.mix_item_2).background as? ColorDrawable)?.color ?: Color.TRANSPARENT
            val underlineColor = (it.findViewById<View>(R.id.underline_2).background as? ColorDrawable)?.color ?: Color.TRANSPARENT

            openMixPlaylist("Mix 2", boxColor, underlineColor)
        }

        view.findViewById<LinearLayout>(R.id.mix_item_3).setOnClickListener {
            val boxColor = (it.findViewById<LinearLayout>(R.id.mix_item_3).background as? ColorDrawable)?.color ?: Color.TRANSPARENT
            val underlineColor = (it.findViewById<View>(R.id.underline_3).background as? ColorDrawable)?.color ?: Color.TRANSPARENT

            openMixPlaylist("Mix 3", boxColor, underlineColor)
        }

        view.findViewById<LinearLayout>(R.id.mix_item_4).setOnClickListener {
            val boxColor = (it.findViewById<LinearLayout>(R.id.mix_item_4).background as? ColorDrawable)?.color ?: Color.TRANSPARENT
            val underlineColor = (it.findViewById<View>(R.id.underline_4).background as? ColorDrawable)?.color ?: Color.TRANSPARENT

            openMixPlaylist("Mix 4", boxColor, underlineColor)
        }

        view.findViewById<LinearLayout>(R.id.mix_item_5).setOnClickListener {
            val boxColor = (it.findViewById<LinearLayout>(R.id.mix_item_5).background as? ColorDrawable)?.color ?: Color.TRANSPARENT
            val underlineColor = (it.findViewById<View>(R.id.underline_5).background as? ColorDrawable)?.color ?: Color.TRANSPARENT

            openMixPlaylist("Mix 5", boxColor, underlineColor)
        }

        //Find user_name
        val userName = view.findViewById<TextView>(R.id.user_name)
        userName.text = dbHelper.getUser(curUser)?.name

    }

    private fun updateRecentlyPlayedAlbums(albums: List<Album>) {
        // Get references to all the album boxes (LinearLayouts) and their content views
        val albumBoxes = listOf(
            boxRecent1 to Pair(albumCover1, albumName1),
            boxRecent2 to Pair(albumCover2, albumName2),
            boxRecent3 to Pair(albumCover3, albumName3),
            boxRecent4 to Pair(albumCover4, albumName4),
            boxRecent5 to Pair(albumCover5, albumName5),
            boxRecent6 to Pair(albumCover6, albumName6)
        )

        // Iterate over the boxes and update content
        for (i in albumBoxes.indices) {
            if (i < albums.size) {
                // Get the album data
                val album = albums[i]

                // Update the album cover and name
                val (coverImageView, nameTextView) = albumBoxes[i].second

                // album image
                Glide.with(this)
                    .load(album.image)
                    .placeholder(R.drawable.blacker_gradient)
                    .into(coverImageView)

                nameTextView.text = album.name // Update album name

                // Show the box since there is an album to display
                albumBoxes[i].first.visibility = View.VISIBLE

                // Add onClickListener to open the album or play its track
                albumBoxes[i].first.setOnClickListener {
                    album.let {
                        // Check if the current fragment is added to the activity before proceeding
                        if (isAdded) {
                            val albumFragment = AlbumFragment.newInstance(album.albumId) //Transfer id

                            loadFragment(albumFragment)
                        } else {
                            Toast.makeText(context, "Fragment not attached to activity", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                // Hide the box if there are not enough albums
                albumBoxes[i].first.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        // Albums RecyclerView setup
        val trackList: List<Track> = recommendSongsFromFrequentArtists(curUser)
        val tracksAdapter =
            TracksAlbumsAdapter(trackList, dbHelper) { track -> openTrack(track.trackId, track.albumId) }

        val recommendationRecyclerView = view.findViewById<RecyclerView>(R.id.recommendation_recyclerview)
        recommendationRecyclerView.layoutManager = LinearLayoutManager(context)
        recommendationRecyclerView.adapter = tracksAdapter
    }

    private fun openTrack(trackId: String, albumId: String? = null) {
        val fragment = SingleTrackFragment.newInstance(trackId,null, albumId)
        loadFragment(fragment)
    }

    private fun recommendSongsFromFrequentArtists(curUser: String): List<Track> {
        val recentlyPlayedTracks = dbHelper.getNewest("track",curUser) as List<Track>

        // Group the tracks by album to fetch the artists and identify if a user has listened to multiple songs from the same artist
        val artistTrackCount = recentlyPlayedTracks
            .map { track -> dbHelper.getAlbum(track.albumId) }  // Fetch the album for each track
            .mapNotNull { album -> album?.artistId }            // Map albums to artist IDs
            .groupingBy { it }                                  // Group by artistId
            .eachCount()                                        // Count how many times each artist appears

        // Find artists the user has listened to more than once
        val frequentArtists = artistTrackCount.filter { it.value > 1 }.keys

        // Recommend other songs from these frequent artists
        val recommendedTracks = mutableListOf<Track>()

        frequentArtists.forEach { artistId ->
            // Fetch all tracks by the artist that are not already in the recently played tracks
            val allTracksByArtist = dbHelper.getTracksByArtist(artistId)

            // Filter out tracks that the user has already listened to
            val newTracks = allTracksByArtist.filter { track ->
                track.trackId !in recentlyPlayedTracks.map { it.trackId }
            }

            recommendedTracks.addAll(newTracks)
        }

        // Optionally, limit recommendations to a certain number of songs
        return recommendedTracks
    }

    // Function to open the playlist corresponding to the selected mix
    private fun openMixPlaylist(mixName: String, boxColor: Int, underlineColor: Int) {
        // Retrieve the playlist ID using the mix name and the user ID
        val userId = "1"  // Replace with the actual user ID
        val playlistId = dbHelper.getPlaylistIdByName(mixName, userId)

        // Check if the playlist ID exists
        if (playlistId != null) {
            // Create an instance of MixPlaylistFragment
            val mixPlaylistFragment = MixPlaylistFragment()

            // Prepare the bundle with playlist ID and colors
            val bundle = Bundle().apply {
                putString("playlist_id", playlistId)
                putInt("box_color", boxColor)
                putInt("underline_color", underlineColor)
            }
            mixPlaylistFragment.arguments = bundle

            // Navigate to the MixPlaylistFragment
            loadFragment(mixPlaylistFragment)
        } else {
            Toast.makeText(context, "Playlist not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleDrawerNavigation(view: View) {
        val settingsButton: TextView = view.findViewById(R.id.settings_button)
        val historyButton: TextView = view.findViewById(R.id.history_button)
        val queueButton: TextView = view.findViewById(R.id.queue_button)
        val logoutButton: TextView = view.findViewById(R.id.logout_button)

        settingsButton.setOnClickListener {
            navigateToSettings()
        }

        historyButton.setOnClickListener {
            navigateToHistory()
        }

        queueButton.setOnClickListener {
            //navigateToQueue()
        }

        logoutButton.setOnClickListener {
            handleLogout()
        }
    }

    private fun navigateToSettings() {
        Toast.makeText(context, "This feature is in development", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToHistory() {
        val recentlyPlayedFragment = RecentlyPlayedFragment()
        loadFragment(recentlyPlayedFragment)
    }

    private fun navigateToQueue() {
        val index = playerViewModel.currentTrackIndex

        if(TrackQueue.queue.isEmpty()) {
            Toast.makeText(context, "There is no queue currently", Toast.LENGTH_SHORT).show()
            return
        }
        val track = TrackQueue.queue[index]
        val playlistId = TrackQueue.queuePlaylistId[index]
        val playlist = dbHelper.getPlaylist(playlistId)

        val queueFragment = QueueFragment.newInstance(track, playlist, null)
        loadFragment(queueFragment)
    }

    private fun handleLogout() {
        //get remember userId
        val editor = sharedPref.edit()

        //get User email and store at the login page
        val userEmail = dbHelper.getUser(curUser)?.email

        editor.remove("curUserId")
        editor.commit()

        drawerLayout.closeDrawer(GravityCompat.START)

        //clear exoPlayer and queue
        TrackQueue.clearQueue()
        exoPlayer.stop()
        exoPlayer.release()

        //notify
        Toast.makeText(context, "Log out successfully.", Toast.LENGTH_SHORT).show()

        //start intent
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra("Email", userEmail)

        //clear data and change activity
        activity.finish()
        activity.startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        // Show the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.showBottomNavigation()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)  // Replace with your fragment container ID
            .addToBackStack(null)  // Optional: Add this transaction to the back stack
            .commit()
        drawerLayout.closeDrawer(GravityCompat.START)
    }
}
