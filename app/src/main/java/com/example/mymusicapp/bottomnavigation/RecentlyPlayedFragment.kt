package com.example.mymusicapp.bottomnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper

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

        // Hide the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }

        val application = requireActivity().application as Global
        curUser = application.curUserId

        // Handle the back button click
        val backButton: ImageButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        // Get albums and playlists
        val databaseHelper = MusicAppDatabaseHelper(requireContext())
        val albumList = databaseHelper.getNewest("album", curUser) as ArrayList<*>
        val playlistList = databaseHelper.getNewest("playlist", curUser) as ArrayList<*>

        // Combine the lists
        val combinedList = ArrayList<Any>()
        combinedList.addAll(albumList)
        combinedList.addAll(playlistList)

        // Set up RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recently_played_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = RecentlyPlayedAdapter(combinedList)
    }
}
