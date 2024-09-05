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
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.SearchResult

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
        // Show a Toast message with the item's name
        Toast.makeText(this, "Clicked on: ${searchResult.name}", Toast.LENGTH_SHORT).show()

        // Navigate based on the result type
        when (searchResult.type) {
            "Artist" -> {
//                val intent = Intent(this, ArtistActivity::class.java)
//                intent.putExtra("artist_id", searchResult.id)
//                startActivity(intent)
            }
            "Album" -> {
//                val intent = Intent(this, AlbumActivity::class.java)
//                intent.putExtra("album_id", searchResult.id)
//                startActivity(intent)
            }
            "Playlist" -> {
//                val intent = Intent(this, PlaylistActivity::class.java)
//                intent.putExtra("playlist_id", searchResult.id)
//                startActivity(intent)
            }
            "Track" -> {
//                val intent = Intent(this, TrackActivity::class.java)
//                intent.putExtra("track_id", searchResult.id)
//                startActivity(intent)
            }
        }
    }
}
