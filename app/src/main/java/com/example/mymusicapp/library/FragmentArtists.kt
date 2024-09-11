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
import com.example.mymusicapp.data.Global
import com.example.mymusicapp.data.MusicAppDatabaseHelper
import com.example.mymusicapp.models.Artist

class FragmentArtists : Fragment(), FragmentArtistAdapter.OnItemClickListener {
    private lateinit var adapter : FragmentArtistAdapter
    private val items = ArrayList<Artist>()
    private lateinit var curUser: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_artists, container, false)
        val application = requireActivity().application as Global
        curUser = application.curUserId
        app(view)

        return view
    }

    private fun app(view: View) {

        //Adapter
        adapter = FragmentArtistAdapter(items, this)
        createArtistItem()
        updateAdapter(sort("ADDED"))

        //Layout Manager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Setup RecyclerView
        val recyclerView : RecyclerView = view.findViewById(R.id.artistsList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        //Sort By
        //Init all sort
        val allSort = ArrayList<String>()
        allSort.addAll(arrayOf("Recently added", "Newest", "A-Z", "Z-A"))

        //Init setOnClickListener
        val sortBut = view.findViewById<TextView>(R.id.sortButtonArtist)
        sortBut.setOnClickListener {
            when (sortBut.text.toString()) {
                allSort[0] -> {
                    sortBut.text = allSort[1]
                }
                allSort[1] -> {
                    //set effect for the button
                    sortBut.text = allSort[2]
                    updateAdapter(sort("ASC"))
                }
                allSort[2] -> {
                    sortBut.text = allSort[3]
                    updateAdapter(sort("DESC"))
                }
                allSort[3] -> {
                    sortBut.text = allSort[0]
                    updateAdapter(sort("ADDED"))
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

    private fun addItem(artist: Artist) {
        items.add(artist)
        adapter.notifyItemInserted(items.size - 1)
    }

    private fun createArtistItem() {
        val db = MusicAppDatabaseHelper(requireContext()) //Get database
        updateAdapter(db.getUserLibraryArtists(curUser))
    }

    override fun setOnItemClickListener(item: Artist?) {
        //Navigate here
        Toast.makeText(context, "Worked!", Toast.LENGTH_SHORT).show()
    }

    private fun sort(order : String): ArrayList<Artist> {
        val db = MusicAppDatabaseHelper(requireContext())
        val sample: ArrayList<Artist> = db.sort("artist", order, curUser) as? ArrayList<Artist>?: ArrayList()
        return sample
    }
}
