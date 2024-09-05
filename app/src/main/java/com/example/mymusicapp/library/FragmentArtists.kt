package com.example.mymusicapp.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Artist

class FragmentArtists : Fragment(), FragmentArtistAdapter.OnItemClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_artists, container, false)

        app(view)

        return view
    }

    private fun app(view: View) {
        //Sort By

        //List
        //Adapter
        val adapter = FragmentArtistAdapter(createArtistItem(), this)

        //Layout Manager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Setup RecyclerView
        val recyclerView : RecyclerView = view.findViewById(R.id.artistsList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun createArtistItem(): ArrayList<Artist> {
        val sample = ArrayList<Artist>()
        val db = MusicAppDatabaseHelper(requireContext()) //Get database

        //
        val allArtistId = db.getAllArtistId()!!
        for(i in 0..<allArtistId.size) {
            val artist = db.getArtist(allArtistId[i])!!
            sample.add(Artist(artist.artistId, artist.name, artist.genre, artist.image))
        }

        //return
        return sample
    }

    override fun setOnItemClickListener(item: Artist?) {
        //Navigate here
        Toast.makeText(context, "Worked!", Toast.LENGTH_SHORT).show()
    }
}
