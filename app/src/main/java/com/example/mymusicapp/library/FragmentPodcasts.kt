package com.example.mymusicapp.library

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R

class FragmentPodcasts : Fragment(), FragmentPodcastAdapter.OnItemClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_podcasts, container, false)

        app(view)

        return view
    }

    private fun app(view: View) {
        //Your liked podcasts, click on image or text to change
        val yourLikedPodcasts : View = view.findViewById(R.id.YourLikedPodcasts)

        yourLikedPodcasts.setOnClickListener {
            //Navigate here
        }

        //Adapter
        val adapter = FragmentPodcastAdapter(createPodcastItem(), this)

        //Layout manager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Setup recyclerView
        val recyclerView : RecyclerView = view.findViewById(R.id.PodcastsList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun createPodcastItem(): ArrayList<PodcastItem> {
        val sample = ArrayList<PodcastItem>()

        //1st Sample
        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.playlistsample)
        sample.add(PodcastItem("Conan Gray", bitmap))

        //2nd Sample
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.chase_atlantic)
        sample.add(PodcastItem("Chase Atlantic", bitmap))

        //return
        return sample
    }

    override fun setOnItemClickListener(item: PodcastItem) {
        //Navigate here
        Toast.makeText(context, "Worked!", Toast.LENGTH_SHORT).show()
    }
}
