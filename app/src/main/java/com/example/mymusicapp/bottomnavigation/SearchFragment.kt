package com.example.mymusicapp.bottomnavigation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R
import com.example.mymusicapp.album.AlbumFragment
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.library.ArtistFragment
import com.example.mymusicapp.models.SearchResult
import com.example.mymusicapp.playlist.PlaylistFragment
import com.example.mymusicapp.playlist.SingleTrackFragment

class SearchFragment : Fragment(), SearchResultAdapter.OnItemClickListener {

    private lateinit var searchInput: EditText
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Set up the back button
        val backButton: ImageButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            // Navigate back by popping the back stack
            activity?.supportFragmentManager?.popBackStack()
        }

        // Implement search functionality
        searchInput = view.findViewById(R.id.search_input)
        searchResultsRecyclerView = view.findViewById(R.id.search_results_recycler_view)

        // Initialize Database Helper
        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Set up the search input and RecyclerView
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString()
                val results = dbHelper.search(keyword)
                searchResultAdapter = SearchResultAdapter(results, this@SearchFragment)
                searchResultsRecyclerView.adapter = searchResultAdapter
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    // Handle item click
    override fun onItemClick(searchResult: SearchResult) {
        // Navigate based on the result type
        when (searchResult.type) {
            "Artist" -> {
                val artistFragment = ArtistFragment.newInstance(searchResult.id) // Transfer artist ID
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, artistFragment)  // Replace with your fragment container ID
                    .addToBackStack(null)  // Optional: Add this transaction to the back stack
                    .commit()
            }

            "Album" -> {
                val albumFragment = AlbumFragment.newInstance(searchResult.id) //Transfer id
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, albumFragment)  // Replace with your fragment container ID
                    .addToBackStack(null)  // Optional: Add this transaction to the back stack
                    .commit()
            }
            "Playlist" -> {
                val playlistFragment = PlaylistFragment.newInstance(searchResult.id) //Transfer id
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, playlistFragment)  // Replace with your fragment container ID
                    .addToBackStack(null)  // Optional: Add this transaction to the back stack
                    .commit()
            }
            "Track" -> {
                val fragment = dbHelper.getTrack(searchResult.id)?.let { track ->
                    SingleTrackFragment.newInstance(
                        track.trackId, null, null)
                }
                if (fragment != null) {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
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
