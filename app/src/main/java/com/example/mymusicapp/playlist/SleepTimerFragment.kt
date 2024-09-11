package com.example.mymusicapp.sleeptimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.example.mymusicapp.MainActivity
import com.example.mymusicapp.R

class SleepTimerFragment : Fragment() {

    private lateinit var timePicker: TimePicker
    private lateinit var setTimerButton: Button
    private lateinit var cancelTimerButton: Button
    private lateinit var timerStatusTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_sleep_timer, container, false)


        // Hide the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }

        return TODO("Provide the return value")
    }


    override fun onDestroyView() {
        super.onDestroyView()

        // Show the navigation bar when this fragment is destroyed
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.showBottomNavigation()
        }
    }


    override fun onResume() {
        super.onResume()
        // Hide the bottom navigation bar when this fragment is resumed
        // Hide the navigation bar when this fragment is created
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.hideBottomNavigation()
        }
    }
}
