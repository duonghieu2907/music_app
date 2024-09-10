package com.example.mymusicapp.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Artist
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.playlist.Add2PlaylistFragment

class MenuFragment : Fragment() {

    private lateinit var track: Track
    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var backButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_menu, container, false)

        backButton = view.findViewById(R.id.back)

        // Set button click handlers
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Retrieve the Track object from the arguments
        track = arguments?.getParcelable("TRACK") ?: throw IllegalStateException("Track not passed to MenuFragment")

        // Find views by ID
        val songCover = view.findViewById<ImageView>(R.id.songCover)
        val songName = view.findViewById<TextView>(R.id.songName)
        val songArtist = view.findViewById<TextView>(R.id.songArtist)

        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Fetch Album and Artist details
        val album: Album? = dbHelper.getAlbum(track.albumId)
        val artist: Artist? = dbHelper.getArtist(album?.artistId ?: "")

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
            openFragment(Add2PlaylistFragment.newInstance(track))  // Pass the track data when opening Add2PlaylistFragment
        }

        addPlaylistIcon.setOnClickListener(clickListener)
        add2PlaylistText.setOnClickListener(clickListener)

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
        fun newInstance(track: Track): MenuFragment {
            val fragment = MenuFragment()
            val args = Bundle()
            args.putParcelable("TRACK", track)  // Pass the Track object to this fragment
            fragment.arguments = args
            return fragment
        }
    }
}
