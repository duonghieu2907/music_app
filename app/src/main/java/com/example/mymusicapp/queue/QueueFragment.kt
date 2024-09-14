package com.example.mymusicapp.queue

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Artist
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.TrackQueue

class QueueFragment : Fragment(), QueueSongAdapter.OnItemClickListener {

    // Declaration of variables...
    private lateinit var recyclerViewQueue: RecyclerView
    private lateinit var recyclerViewPlaylist: RecyclerView
    private lateinit var queueSongAdapter: QueueSongAdapter

    private lateinit var backButton: ImageView
    private lateinit var clearQueueButton: TextView
    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var track: Track
    private lateinit var footer: View // Corrected ConstraintLayout to View type
    private var playlist: Playlist? = null
    private var album: Album? = null

    private lateinit var removeButton: TextView  // Add this if needed
    private lateinit var add2QButton: TextView  // Add this if needed
    private lateinit var selectedTrack: Track  // Variable to store selected track

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_queue, container, false)

        // Hide the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }

        // Initialize the database helper
        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Set up UI elements and receive arguments...
        backButton = view.findViewById(R.id.back)
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Initialize removeButton
        removeButton = view.findViewById(R.id.removeButton)
        removeButton.setOnClickListener {
            val selectedTrack = queueSongAdapter.getSelectedTrack() // Get the selected track
            if (selectedTrack != null) {
                removeSelectedTrack(selectedTrack) // Remove the selected track
            } else {
                Log.d("QueueFragment", "No track selected to remove.")
            }
        }


        // Initialize removeButton
        add2QButton = view.findViewById(R.id.add2QueueButton)
        add2QButton.setOnClickListener {
            addSelectedTrack() // Call the method to add the selected track
        }


        // Set up UI elements and receive arguments...
        clearQueueButton = view.findViewById(R.id.clearQueueButton)
        clearQueueButton.setOnClickListener {
            TrackQueue.clearQueue()

            // Reload the fragment
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().apply {
                // Replace the current fragment with a new instance of QueueFragment
                replace(R.id.fragment_container, QueueFragment.newInstance(track, playlist, album))
                // Optional: Add this to the back stack if you want users to go back
                // addToBackStack(null)
                commit()
            }
        }



        footer = view.findViewById(R.id.footer)

        // Receive arguments passed to this fragment
        val imageUrl = arguments?.getString("IMAGE_URL") ?: "default_url"
        val songNameText = arguments?.getString("SONG_NAME") ?: "Default Song Name"
        val artistNameText = arguments?.getString("ARTIST_NAME") ?: "Default Artist Name"
        val playlistNameText = arguments?.getString("PLAYLIST_NAME") ?: "Default Playlist Name"

        val songCover = view.findViewById<ImageView>(R.id.songCover)
        val songName = view.findViewById<TextView>(R.id.songPlaying)
        val songArtist = view.findViewById<TextView>(R.id.artistSongPlaying)
        val playlistName = view.findViewById<TextView>(R.id.playlistName)
        val playingFrom = view.findViewById<TextView>(R.id.playingFrom)

        // Fetch the Track and Playlist passed from arguments
        track = arguments?.getParcelable("TRACK") ?: return view
        playlist = arguments?.getParcelable("PLAYLIST")
        album = arguments?.getParcelable("ALBUM")

        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Fetch Album and Artist information
        val album1: Album? = dbHelper.getAlbum(track.albumId)
        val artist: Artist? = dbHelper.getArtist(album1?.artistId ?: "")

        // Load the album cover image
        Glide.with(this)
            .load(album1?.image)
            .placeholder(R.drawable.blacker_gradient)
            .into(songCover)

        songName.text = track.name


        songArtist.text = artist?.name ?: "Unknown Artist"



        if(playlist != null)
        {
            playlistName.text = playlist?.name
            playingFrom.text = "PLAYING FROM PLAYLIST"
        }
        else if (album != null)
        {
            playlistName.text = album?.name
            playingFrom.text = "PLAYING FROM ALBUM"
        }
        else {
            playlistName.text = ""
            playingFrom.text = ""
        }

        // Set up RecyclerViews for queue and playlist
        recyclerViewQueue = view.findViewById(R.id.recyclerViewSongsQueue)
        recyclerViewQueue.layoutManager = LinearLayoutManager(requireContext())
        queueSongAdapter = QueueSongAdapter(dbHelper) { track ->
            if (track != null) {
                handleTrackSelection(track)
            } // Set selected track when clicked
        }
        recyclerViewQueue.adapter = queueSongAdapter

        // Set click listeners for the adapter
        queueSongAdapter.setOnItemClickListener(this)

        //recyclerViewPlaylist = view.findViewById(R.id.recyclerViewSongsPlaylist)
        //recyclerViewPlaylist.layoutManager = LinearLayoutManager(requireContext())

//        val playlistTracks = playlist?.let { dbHelper.getTracksByPlaylistId(it.playlistId) }
//        val currentTrackIndex = playlistTracks?.indexOfFirst { it.trackId == track.trackId } ?: 0
//        playlistSongAdapter = PlaylistSongAdapter(dbHelper, playlist, currentTrackIndex)
//        { track ->
//            handleTrackSelection(track) // Set selected track when clicked
//        }
//        recyclerViewPlaylist.adapter = playlistSongAdapter

        // Set click listeners for both adapters
        queueSongAdapter.setOnItemClickListener(this)
 //       playlistSongAdapter.setOnItemClickListener(this)

        return view
    }

    companion object {
        fun newInstance(track: Track, playlist: Playlist?, album: Album?): QueueFragment {
            val fragment = QueueFragment()
            val args = Bundle().apply {
                putParcelable("TRACK", track)
                if (playlist != null) {
                    putParcelable("PLAYLIST", playlist)
                }
                if (album != null) {
                    putParcelable("ALBUM", album)
                }
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(position: Int) {
        Log.d("QueueFragment", "Item clicked at position: $position")
        adjustFooterMargin(0)
    }



    private fun adjustFooterMargin(marginBottomDp: Int) {
        val layoutParams = footer.layoutParams as ViewGroup.MarginLayoutParams
        val marginBottomPx = (marginBottomDp * resources.displayMetrics.density).toInt()
        layoutParams.bottomMargin = marginBottomPx
        footer.layoutParams = layoutParams
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


    private fun handleTrackSelection(track: Track) {
        selectedTrack = track  // Store the selected track
        Log.d("QueueFragment", "Selected track: ${track.name}")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeSelectedTrack(track: Track) {
        TrackQueue.removeTrack(track) // Remove from TrackQueue
        queueSongAdapter.resetSelection()  // Reset selection after removal
        queueSongAdapter.notifyDataSetChanged() // Notify adapter of data change
        Log.d("QueueFragment", "Track removed: ${track.name}")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addSelectedTrack() {
        if (::selectedTrack.isInitialized) { // Check if selectedTrack is initialized
            TrackQueue.addTrack(selectedTrack, "") // Add from TrackQueue
            queueSongAdapter.notifyDataSetChanged() // Notify adapter of data change

            Log.d("QueueFragment", "Track added: ${selectedTrack.name}")
        } else {
            Log.d("QueueFragment", "No track selected to add.")
        }
    }

}
