package com.example.mymusicapp.playlist
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mymusicapp.R
import com.squareup.picasso.Picasso

class SingleSongFragment : Fragment() {

    companion object {
        private const val ARG_SONG = "song"

        fun newInstance(song: Song): SingleSongFragment {
            val fragment = SingleSongFragment()
            val args = Bundle()
            args.putParcelable(ARG_SONG, song)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var song: Song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            song = it.getParcelable(ARG_SONG)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_single_song, container, false)

        val songTitle: TextView = view.findViewById(R.id.song_title)
        val artistName: TextView = view.findViewById(R.id.artist_name)
        val songCover: ImageView = view.findViewById(R.id.song_cover)
        val lyricsText: TextView = view.findViewById(R.id.lyrics_text)
        val songEndTime: TextView = view.findViewById(R.id.song_end_time)

        songTitle.text = song.title
        artistName.text = song.artist
        songEndTime.text = song.duration
        lyricsText.text = song.lyrics
        Picasso.get().load(song.imageUrl).into(songCover)

        return view
    }
}
