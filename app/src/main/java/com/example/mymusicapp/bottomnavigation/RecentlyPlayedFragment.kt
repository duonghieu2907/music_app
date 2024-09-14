package com.example.mymusicapp.bottomnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.playlist.SingleTrackFragment

class RecentlyPlayedFragment : Fragment() {
    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var curUser: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize DBHelper
        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Initialize UI
        val backButton = view.findViewById<ImageView>(R.id.back_button)

        val application = requireActivity().application as Global
        curUser = application.curUserId!!


        // Set up back button
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Set up RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recently_played_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch tracks for this album
        val trackList = dbHelper.getNewest("track", curUser) as List<Track>
        val tracksAdapter =
            TracksAlbumsAdapter(trackList, dbHelper) { track -> openTrack(track.trackId, track.albumId) }
        recyclerView.adapter = tracksAdapter
    }

    private fun openTrack(trackId: String, albumId: String? = null) {
        val fragment = SingleTrackFragment.newInstance(trackId,null, albumId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        // Hide the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }
    }
}
