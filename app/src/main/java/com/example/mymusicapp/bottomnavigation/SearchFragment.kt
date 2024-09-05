package com.example.mymusicapp.bottomnavigation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.SearchResult
import com.example.mymusicapp.playlist.PlaylistFragment

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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the back button
        val backButton: ImageButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            // Handle back button click
            parentFragmentManager.popBackStack()
        }

        // Implement search functionality
        searchInput = view.findViewById(R.id.search_input)
        searchResultsRecyclerView = view.findViewById(R.id.search_results_recycler_view)

        // Initialize Database Helper
        dbHelper = MusicAppDatabaseHelper(requireContext())

        // Set up the search input and RecyclerView
        searchInput = view.findViewById(R.id.search_input)
        searchResultsRecyclerView = view.findViewById(R.id.search_results_recycler_view)
        dbHelper = MusicAppDatabaseHelper(requireContext())

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
    }
    // Handle item click
    override fun onItemClick(searchResult: SearchResult) {
        // Show a Toast message with the item's name
        Toast.makeText(requireContext(), "Clicked on: ${searchResult.name}", Toast.LENGTH_SHORT).show()

        // Navigate based on the result type
        when (searchResult.type) {
            "Artist" -> {
//                val artistFragment = ArtistFragment()
//                val bundle = Bundle()
//                bundle.putString("artist_id", searchResult.id)
//                artistFragment.arguments = bundle
//                parentFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, artistFragment)
//                    .addToBackStack(null)
//                    .commit()
            }
            "Album" -> {
//                val albumFragment = AlbumFragment()
//                val bundle = Bundle()
//                bundle.putString("album_id", searchResult.id)
//                albumFragment.arguments = bundle
//                parentFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, albumFragment)
//                    .addToBackStack(null)
//                    .commit()
            }
            "Playlist" -> {
                val playlistFragment = PlaylistFragment()
                val bundle = Bundle()
                bundle.putString("playlist_id", searchResult.id)
                playlistFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, playlistFragment)
                    .addToBackStack(null)
                    .commit()
            }
            "Track" -> {
//                val trackFragment = TrackFragment()
//                val bundle = Bundle()
//                bundle.putString("track_id", searchResult.id)
//                trackFragment.arguments = bundle
//                parentFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, trackFragment)
//                    .addToBackStack(null)
//                    .commit()
            }
        }
    }
}
