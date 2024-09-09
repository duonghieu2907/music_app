package com.example.mymusicapp.bottomnavigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicapp.R
import com.example.mymusicapp.library.BrowseLibrary
import com.example.mymusicapp.library.FragmentAlbums
import com.example.mymusicapp.library.FragmentArtists
import com.example.mymusicapp.library.FragmentLibraryFilterAdapter
import com.example.mymusicapp.library.FragmentPlaylists
import com.example.mymusicapp.library.FragmentPodcasts
import com.example.mymusicapp.library.FragmentYourLibrary
import com.example.mymusicapp.library.LibraryFilterItem

class LibraryFragment : Fragment(), FragmentLibraryFilterAdapter.FragmentLibraryFilterSelectionListener {
    private lateinit var drawerLayout: DrawerLayout
 
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_library, container, false)
        app(view)

        return view

    }

    private fun app(view: View) {
        // Reference to drawer layout
        drawerLayout = requireActivity().findViewById(R.id.main)

        // Avatar icon
        val avatarIcon = view.findViewById<ImageView>(R.id.avatar_icon)
        avatarIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
      
//        //Implement search icon
//        val searchIcon: ImageButton = view.findViewById(R.id.search_icon)
//        searchIcon.setOnClickListener {
//            //Going to search fragment
//            val intent = Intent(context, BrowseLibrary::class.java)
//            startActivity(intent)
//        }

        //Implement item filter
        //Init adapter
        val itemFilterAdapter = FragmentLibraryFilterAdapter(createItemFilter(), this)

        //Init layoutManager
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //Setup recycleView
        val recyclerView: RecyclerView = view.findViewById(R.id.libraryFilter)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = itemFilterAdapter


        //Your library Fragment
        val yourLibraryText : TextView = view.findViewById(R.id.YourLibraryText)
        yourLibraryText.setOnClickListener {
            onSelectionListener(null)
            itemFilterAdapter.resetColors()
        }

        val yourLibraryIcon : ImageView = view.findViewById(R.id.YourLibraryIcon)
        yourLibraryIcon.setOnClickListener {
            onSelectionListener(null)
            itemFilterAdapter.resetColors()
        }
        //Set up list
        //Default layout, calling once
        onSelectionListener(null)

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

    override fun onSelectionListener(item: LibraryFilterItem?) {
        //Change fragment depends on the item clicks
        when(item?.name) {
            "Playlists" -> loadFragment(FragmentPlaylists())
            "Artists" -> loadFragment(FragmentArtists())
            "Albums" -> loadFragment(FragmentAlbums())
            "Podcasts" -> loadFragment(FragmentPodcasts())
            else -> loadFragment(FragmentYourLibrary())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_library_container_list, fragment)
            .commit()
    }
}
