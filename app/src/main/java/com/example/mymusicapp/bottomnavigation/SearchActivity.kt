package com.example.mymusicapp.bottomnavigation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import com.example.mymusicapp.album.AlbumFragment
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.library.ArtistFragment
import com.example.mymusicapp.models.SearchResult
import com.example.mymusicapp.playlist.PlaylistFragment
import com.example.mymusicapp.playlist.SingleTrackFragment

class SearchActivity : AppCompatActivity(), SearchResultAdapter.OnItemClickListener {

    private lateinit var searchInput: EditText
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var dbHelper: MusicAppDatabaseHelper
    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Set up the back button
        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            // Finish activity on back button click
            finish()
        }

        // Implement search functionality
        searchInput = findViewById(R.id.search_input)
        searchResultsRecyclerView = findViewById(R.id.search_results_recycler_view)

        // Initialize Database Helper
        dbHelper = MusicAppDatabaseHelper(this)

        // Set up the search input and RecyclerView
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString()
                val results = dbHelper.search(keyword)
                searchResultAdapter = SearchResultAdapter(results, this@SearchActivity)
                searchResultsRecyclerView.adapter = searchResultAdapter
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Handle item click
    override fun onItemClick(searchResult: SearchResult) {
        // Navigate based on the result type
        when (searchResult.type) {
            "Artist" -> {
                val artistFragment = ArtistFragment.newInstance(searchResult.id) // Transfer artist ID
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_search, artistFragment)  // Replace with your fragment container ID
                    .addToBackStack(null)  // Optional: Add this transaction to the back stack
                    .commit()

                Toast.makeText(this, "Clicked on Artist: ${searchResult.name}", Toast.LENGTH_SHORT).show()
            }

            "Album" -> {
                val albumFragment = AlbumFragment.newInstance(searchResult.id) //Transfer id
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_search, albumFragment)  // Replace with your fragment container ID
                    .addToBackStack(null)  // Optional: Add this transaction to the back stack
                    .commit()

                Toast.makeText(this, "Clicked on Album: ${searchResult.name}", Toast.LENGTH_SHORT).show()
            }
            "Playlist" -> {
                val playlistFragment = PlaylistFragment.newInstance(searchResult.id) //Transfer id
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_search, playlistFragment)  // Replace with your fragment container ID
                    .addToBackStack(null)  // Optional: Add this transaction to the back stack
                    .commit()

                Toast.makeText(this, "Clicked on Playlist: ${searchResult.name}", Toast.LENGTH_SHORT).show()
            }
            "Track" -> {
                val fragment = dbHelper.getTrack(searchResult.id)?.let { track ->
                    SingleTrackFragment.newInstance(
                        track.trackId, null, null)
                }
                if (fragment != null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_search, fragment)
                        .addToBackStack(null)
                        .commit()
                }
                Toast.makeText(this, "Clicked on Track: ${searchResult.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
