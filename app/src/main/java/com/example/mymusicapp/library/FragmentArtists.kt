package com.example.mymusicapp.library

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
class FragmentArtists : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_artists, container, false)

        App(view)

        return view
    }

    private fun App(view: View) {
        //Sort By

        //List
        //Adapter
        val adapter = FragmentArtistAdapter(createArtistItem())

        //Layout Manager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Setup RecyclerView
        val recyclerView : RecyclerView = view.findViewById(R.id.artistsList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun createArtistItem(): ArrayList<ArtistItem> {
        val sample = ArrayList<ArtistItem>()

        //1st Sample
        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.playlistsample)
        sample.add(ArtistItem("Conan Gray", bitmap))

        //2nd Sample
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.chase_atlantic)
        sample.add(ArtistItem("Chase Atlantic", bitmap))

        //return
        return sample
    }
}
