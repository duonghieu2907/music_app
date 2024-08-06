package com.example.mymusicapp.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mymusicapp.R
class FragmentYourLibrary : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_your_library, container, false)
        //App(view)

        return view
    }

    private fun App(view: View) {
        TODO("Not yet implemented")
    }
}