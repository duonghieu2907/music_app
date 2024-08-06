package com.example.mymusicapp.bottomnavigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import com.example.mymusicapp.R
import com.example.mymusicapp.library.BrowseLibrary

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
    }
    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit();
    }
}
