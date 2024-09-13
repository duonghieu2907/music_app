package com.example.mymusicapp.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Playlist

class FragmentNewPlaylist : Fragment() {

    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var etPlaylistName: EditText
    private lateinit var etPlaylistCoverUrl: EditText
    private lateinit var cancelButton: AppCompatButton
    private lateinit var createButton: AppCompatButton

    private var trackId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_playlist, container, false)

        // Hide the bottom navigation bar when this fragment is displayed
        (requireActivity() as MainActivity).hideBottomNavigation()

        // Initialize UI components
        etPlaylistName = view.findViewById(R.id.etPlaylistName)
        etPlaylistCoverUrl = view.findViewById(R.id.etPlaylistCoverUrl)
        cancelButton = view.findViewById(R.id.cancelButton)
        createButton = view.findViewById(R.id.createButton)

        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Get trackId from arguments
        trackId = arguments?.getString("TRACK_ID")

        cancelButton.setOnClickListener {
            // Handle cancel action
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        createButton.setOnClickListener {
            // Handle create playlist action
            createNewPlaylist()
        }

        return view
    }

    private fun createNewPlaylist() {
        val playlistName = etPlaylistName.text.toString().trim()
        val coverUrl = etPlaylistCoverUrl.text.toString().trim()

        if (playlistName.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a playlist name", Toast.LENGTH_SHORT).show()
            return
        }

        val app = requireActivity().application as Global

        // Fetch the current user's ID (assuming you have an app-level reference to the current user)
        val curUserId = app.curUserId

        // Get the count of playlists for the current user
        val playlistCount = dbHelper.getPlaylistCountByUser(curUserId)

        // Create a new Playlist object with all required parameters
        val newPlaylist = Playlist(
            playlistId = generatePlaylistId(curUserId, playlistCount), // Generate a unique ID for the playlist
            userId = curUserId,                   // Provide the user ID
            name = playlistName,               // Playlist name
            image = coverUrl                   // Cover URL as the image
        )



        // Insert the new playlist into the database
        val result = dbHelper.addPlaylist(newPlaylist)

        if (result.isNotEmpty()) {  // Check if the playlist was successfully added (i.e., result is not empty)
            Toast.makeText(requireContext(), "Playlist created successfully!", Toast.LENGTH_SHORT).show()

            // If trackId is not null, add the track to the newly created playlist
            trackId?.let { trackId ->
                dbHelper.addPlaylistTrack(newPlaylist.playlistId, trackId)
                Toast.makeText(requireContext(), "Track added to the new playlist!", Toast.LENGTH_SHORT).show()
            }

            // Go back to the previous fragment
            requireActivity().onBackPressedDispatcher.onBackPressed()
        } else {
            Toast.makeText(requireContext(), "Failed to create playlist", Toast.LENGTH_SHORT).show()
        }
    }

    // Update this method to accept userId and playlistCount as parameters
    private fun generatePlaylistId(userId: String, playlistCount: Int): String {
        return "$userId*${playlistCount + 1}" // Generate unique playlistId
    }


    companion object {
        fun newInstance(trackId: String? = null): FragmentNewPlaylist {
            val fragment = FragmentNewPlaylist()
            val args = Bundle()
            args.putString("TRACK_ID", trackId)
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
}
