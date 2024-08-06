package com.example.mymusicapp.bottomnavigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.example.mymusicapp.R
import com.example.mymusicapp.library.BrowseLibrary
import com.example.mymusicapp.library.FragmentLibraryFilterAdapter
import com.example.mymusicapp.library.LibraryFilterItem

class LibraryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_library, container, false)
        App(view);

        return view;
    }

    private fun App(view: View) {
        //Implement search icon
        val toolbarContainer: FrameLayout = view.findViewById(R.id.toolNav)
        val search_icon: ImageButton = toolbarContainer.findViewById(R.id.search_icon);
        search_icon.setOnClickListener {
            //Going to search fragment
            val intent: Intent = Intent(context, BrowseLibrary::class.java);
            startActivity(intent);
        }

        //Implement item filter
        //Init adapter
        val itemFilterAdapter = FragmentLibraryFilterAdapter(createItemFilter())

        //Init layourManager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //Setup recycleView
        val recyclerView: RecyclerView = view.findViewById(R.id.libraryFilter)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = itemFilterAdapter


    }
    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit();
    }

    //Create the item list
    private fun createItemFilter() : ArrayList<LibraryFilterItem> {
        val items = ArrayList<LibraryFilterItem>()
        items.add(LibraryFilterItem("Playlists"))
        items.add(LibraryFilterItem("Artists"))
        items.add(LibraryFilterItem("Albums"))
        items.add(LibraryFilterItem("Podcasts"))

        return items
    }
}
