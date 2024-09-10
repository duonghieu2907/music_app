package com.example.mymusicapp.queue

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
import com.example.mymusicapp.playlist.PlaylistSongAdapter

class QueueFragment : Fragment(), QueueSongAdapter.OnItemClickListener, PlaylistSongAdapter.OnItemClickListener {

    // Declaration of variables...
    private lateinit var recyclerViewQueue: RecyclerView
    private lateinit var recyclerViewPlaylist: RecyclerView
    private lateinit var queueSongAdapter: QueueSongAdapter
    private lateinit var playlistSongAdapter: PlaylistSongAdapter
    private lateinit var backButton: ImageView
    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var track: Track
    private lateinit var footer: View // Corrected ConstraintLayout to View type
    private var playlist: Playlist? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_queue, container, false)

        // Hide the navigation bar when this fragment is created
        (requireActivity() as MainActivity).hideBottomNavigation()

        // Set up UI elements and receive arguments...
        backButton = view.findViewById(R.id.back)
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
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

        // Fetch the Track and Playlist passed from arguments
        track = arguments?.getParcelable("TRACK") ?: return view
        playlist = arguments?.getParcelable("PLAYLIST")

        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Fetch Album and Artist information
        val album: Album? = dbHelper.getAlbum(track.albumId)
        val artist: Artist? = dbHelper.getArtist(album?.artistId ?: "")

        // Load the album cover image
        Glide.with(this)
            .load(album?.image)
            .placeholder(R.drawable.blacker_gradient)
            .into(songCover)

        songName.text = track.name
        songArtist.text = artist?.name ?: "Unknown Artist"
        playlistName.text = playlist?.name ?: ""

        // Set up RecyclerViews for queue and playlist
        recyclerViewQueue = view.findViewById(R.id.recyclerViewSongsQueue)
        recyclerViewQueue.layoutManager = LinearLayoutManager(requireContext())
        queueSongAdapter = QueueSongAdapter(TrackQueue, dbHelper)
        recyclerViewQueue.adapter = queueSongAdapter

        recyclerViewPlaylist = view.findViewById(R.id.recyclerViewSongsPlaylist)
        recyclerViewPlaylist.layoutManager = LinearLayoutManager(requireContext())

        val playlistTracks = playlist?.let { dbHelper.getTracksByPlaylistId(it.playlistId) }
        val currentTrackIndex = playlistTracks?.indexOfFirst { it.trackId == track.trackId } ?: 0
        playlistSongAdapter = PlaylistSongAdapter(dbHelper, playlist, currentTrackIndex)
        recyclerViewPlaylist.adapter = playlistSongAdapter

        // Set click listeners for both adapters
        queueSongAdapter.setOnItemClickListener(this)
        playlistSongAdapter.setOnItemClickListener(this)

        return view
    }

    companion object {
        fun newInstance(track: Track, playlist: Playlist?): QueueFragment {
            val fragment = QueueFragment()
            val args = Bundle().apply {
                putParcelable("TRACK", track)
                if (playlist != null) {
                    putParcelable("PLAYLIST", playlist)
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
        (requireActivity() as MainActivity).showBottomNavigation()
    }


    override fun onResume() {
        super.onResume()
        // Show the bottom navigation bar when this fragment is resumed
        (requireActivity() as MainActivity).hideBottomNavigation()
    }
}
