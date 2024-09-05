package com.example.mymusicapp.bottomnavigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymusicapp.R
import com.example.mymusicapp.models.SearchResult

class SearchResultAdapter(
    private val searchResults: List<SearchResult>,
    private val onItemClickListener: SearchFragment
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(searchResult: SearchResult)
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImageView: ImageView = view.findViewById(R.id.item_cover)
        val nameTextView: TextView = view.findViewById(R.id.item_name)
        val subNameTextView: TextView = view.findViewById(R.id.item_sub_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = searchResults[position]
        holder.nameTextView.text = result.name

        // Load the image with Glide
        Glide.with(holder.coverImageView.context)
            .load(result.image) // Load image from URL
            .placeholder(R.drawable.album_cover_ttpd) // Placeholder image while loading
            .error(R.drawable.album_cover_ttpd) // Error image if the load fails
            .into(holder.coverImageView)

        // Handle different types (Artist, Album, Playlist)
        when (result.type) {
            "Artist" -> {
                holder.subNameTextView.visibility = View.GONE
                // Making the image round
                holder.coverImageView.clipToOutline = true
            }
            "Album", "Playlist" -> {
                holder.subNameTextView.text = result.subName // Set artist/owner name
                holder.subNameTextView.visibility = View.VISIBLE
                holder.coverImageView.clipToOutline = false
            }
        }
        // Set the item click listener
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(result)
        }
    }

    override fun getItemCount() = searchResults.size
}


