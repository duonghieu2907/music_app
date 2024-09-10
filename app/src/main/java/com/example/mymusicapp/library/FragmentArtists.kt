package com.example.mymusicapp.library

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Artist

class FragmentArtists : Fragment(), FragmentArtistAdapter.OnItemClickListener {
    private lateinit var adapter : FragmentArtistAdapter
    private val items = ArrayList<Artist>()
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

        //Adapter
        items.addAll(createArtistItem())
        adapter = FragmentArtistAdapter(items, this)

        //Layout Manager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Setup RecyclerView
        val recyclerView : RecyclerView = view.findViewById(R.id.artistsList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        //Sort By
        //Init all sort
        val allSort = ArrayList<String>()
        allSort.add("Recently played")
        allSort.add("A-Z")
        allSort.add("Z-A")

        //Init setOnClickListener
        val sortBut = view.findViewById<TextView>(R.id.sortButtonArtist)
        sortBut.setOnClickListener {
            when (sortBut.text.toString()) {
                allSort[0] -> {
                    //set effect for the button
                    sortBut.text = allSort[1]
                    updateAdapter(sort("ASC"))
                }
                allSort[1] -> {
                    sortBut.text = allSort[2]
                    updateAdapter(sort("DESC"))
                }
                allSort[2] -> {
                    sortBut.text = allSort[0]
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateAdapter(list : ArrayList<Artist>) {
        items.clear()
        items.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun createArtistItem(): ArrayList<Artist> {
        val sample = ArrayList<Artist>()
        val db = MusicAppDatabaseHelper(requireContext()) //Get database

        //put all artists from database to list
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

    private fun sort(order : String): ArrayList<Artist> {
        val db = MusicAppDatabaseHelper(requireContext())
        val sample: ArrayList<Artist> = db.sort("artist", order) as? ArrayList<Artist>?: ArrayList()
        return sample
    }
}
