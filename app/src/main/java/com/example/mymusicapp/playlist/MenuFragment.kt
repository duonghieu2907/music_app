package com.example.mymusicapp.playlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.album.AlbumFragment
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.library.ArtistFragment
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Artist
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.TrackQueue

class MenuFragment : Fragment() {

    private lateinit var track: Track
    private  var playlist: Playlist? = null
    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var backButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_menu, container, false)

        // Hide the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }

        backButton = view.findViewById(R.id.back)

        // Set button click handlers
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }



        // Find views by ID
        val songCover = view.findViewById<ImageView>(R.id.songCover)
        val songName = view.findViewById<TextView>(R.id.songName)
        val songArtist = view.findViewById<TextView>(R.id.songArtist)

        dbHelper = MusicAppDatabaseHelper(requireContext())


        // Fetch track, playlist, and album using IDs
        val trackId = arguments?.getString("TRACK_ID") ?: return view
        val playlistId = arguments?.getString("PLAYLIST_ID") ?: ""
        var albumId = arguments?.getString("ALBUM_ID")

        // Fetch objects from the database using their IDs
        track = dbHelper.getTrack(trackId) ?: return view
        playlist = playlistId.let { dbHelper.getPlaylist(it) }


        // Fetch Album and Artist details
        val album: Album? = dbHelper.getAlbum(track.albumId)
        val artist: Artist? = dbHelper.getArtist(album?.artistId ?: "")

        //Assume going from other fragments, which are not from albums
        if(albumId == null) albumId = album?.albumId

        // Set the values for the songCover, songName, and songArtist
        val imageUrl = "https://i.scdn.co/image/ab67616d0000b273356c22ef2466bb450b32e1bb"
        Glide.with(this)
            .load(album?.image)
            .placeholder(R.drawable.blacker_gradient)
            .error(R.drawable.blacker_gradient)
            .into(songCover)

        songName.text = track.name
        songArtist.text = artist?.name ?: "Unknown Artist"

        // Find views by ID for the menu items
        val addPlaylistIcon = view.findViewById<ImageView>(R.id.addPlaylist)
        val add2PlaylistText = view.findViewById<TextView>(R.id.add2Playlist)

        // Set click listeners for the menu items to open Add2PlaylistFragment
        val clickListener = View.OnClickListener {
            openFragment(Add2PlaylistFragment.newInstance(track ))  // Pass the track data when opening Add2PlaylistFragment
        }

        addPlaylistIcon.setOnClickListener(clickListener)
        add2PlaylistText.setOnClickListener(clickListener)


        // Find views by ID for the menu items
        val addQIcon = view.findViewById<ImageView>(R.id.addQ)
        val add2QText = view.findViewById<TextView>(R.id.add2Q)



        val clickListenerQ = View.OnClickListener {
            TrackQueue.addTrack(track, playlistId)
        }


        addQIcon.setOnClickListener(clickListenerQ)
        add2QText.setOnClickListener(clickListenerQ)


        val app = requireActivity().application as Global

        // Fetch the current user's ID (assuming you have an app-level reference to the current user)
        val curUserId = app.curUserId

        // Check if the playlist is null, or the user doesn't own it, or it's the liked songs playlist
        if (playlist == null || playlist!!.userId != curUserId || playlistId == "userLikedSongs") {
            // Find the views for the "remove from playlist" option
            val removeFromPlaylistIcon = view.findViewById<ImageView>(R.id.removePlaylist)
            val removeFromPlaylistText = view.findViewById<TextView>(R.id.removeFromPlaylist)

            if (playlistId == "willneverused") {
                // Set click listener for unliking the song
                val clickListenerUnlike = View.OnClickListener {



                    if (dbHelper.isTrackLiked(track.trackId, curUserId)) {
                        dbHelper.deleteLike(curUserId, track.trackId) // Use global curUserId
                    }



                    Toast.makeText(requireContext(), "Track unliked", Toast.LENGTH_SHORT).show()
                }
                removeFromPlaylistIcon.setOnClickListener(clickListenerUnlike)
                removeFromPlaylistText.setOnClickListener(clickListenerUnlike)

            } else {
                // Tint the icon and text to gray for non-removable playlists
                removeFromPlaylistIcon.setColorFilter(
                    resources.getColor(R.color.DavysGrey, requireContext().theme),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                removeFromPlaylistText.setTextColor(resources.getColor(R.color.DavysGrey, requireContext().theme))

                // Disable click actions
                removeFromPlaylistIcon.isClickable = false
                removeFromPlaylistText.isClickable = false
            }

        } else {
            // If the playlist is not null and the user owns it, set up click listeners as usual
            val removeFromPlaylistIcon = view.findViewById<ImageView>(R.id.removePlaylist)
            val removeFromPlaylistText = view.findViewById<TextView>(R.id.removeFromPlaylist)

            val clickListenerRemoveFromPlaylist = View.OnClickListener {
                dbHelper.deletePlaylistTrack(playlist!!.playlistId, track.trackId)
                Toast.makeText(requireContext(), "Track removed from ${playlist!!.name}", Toast.LENGTH_SHORT).show()
            }

            removeFromPlaylistIcon.setOnClickListener(clickListenerRemoveFromPlaylist)
            removeFromPlaylistText.setOnClickListener(clickListenerRemoveFromPlaylist)
        }

        //View artist
        val viewArtist: ImageView = view.findViewById(R.id.viewArtist)
        val viewArtistText: TextView = view.findViewById(R.id.viewAnArtist)

        val onArtistClickListener = View.OnClickListener {
            if(artist?.artistId == null) {
                Log.e("MenuFragment", "There is no artist")
                Toast.makeText(requireContext(), "There is no artist", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val fragment = ArtistFragment.newInstance(artist.artistId)
            openFragment(fragment)
        }
        viewArtist.setOnClickListener(onArtistClickListener)
        viewArtistText.setOnClickListener(onArtistClickListener)


        //View album
        val viewAlbum: ImageView = view.findViewById(R.id.viewAlbum)
        val viewAlbumText: TextView = view.findViewById(R.id.viewAnAlbum)

        val onAlbumClickListener = View.OnClickListener {
            if(albumId == null) {
                Log.e("MenuFragment", "There is no album")
                Toast.makeText(requireContext(), "There is no album", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val fragment = AlbumFragment.newInstance(albumId)
            openFragment(fragment)
        }

        viewAlbum.setOnClickListener(onAlbumClickListener)
        viewAlbumText.setOnClickListener(onAlbumClickListener)

        return view
    }

    // Helper function to open fragments
    private fun openFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment) // Replace with the ID of your container
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    companion object {
        fun newInstance(trackId: String, playlistId: String? = null): MenuFragment {
            val fragment = MenuFragment()
            val args = Bundle()
            args.putString("TRACK_ID", trackId)
            playlistId?.let { args.putString("PLAYLIST_ID", it) }

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
}
