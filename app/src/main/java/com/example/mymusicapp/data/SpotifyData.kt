package com.example.mymusicapp.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.models.SpotifyPublicUser
import com.adamratzman.spotify.spotifyAppApi
import com.example.mymusicapp.library.AlbumItem
import com.example.mymusicapp.library.PlaylistListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class SpotifyData {
    private val clientID = "95347603f19a497aa51c78b2a3dd81d3"
    private val clientSecret = "ca5f8fa71e8542a98035ded2caa67efa"
    private var api: SpotifyAppApi? = null
    suspend fun buildSearchApi() {
        api = spotifyAppApi(clientID, clientSecret).build()
    }

    suspend fun findAlbums(): ArrayList<AlbumItem>? {
        try {
            val albums = ArrayList<AlbumItem>()
            val album = api?.albums?.getAlbum("3iPSVi54hsacKKl1xIR2eH")
            val imageURL: String = album!!.images[0].url
            val image = loadUrl(imageURL)
            val item = AlbumItem(album.name, image)

            albums.add(item)
            println("Success")
            return albums
        } catch (e: Exception) {
            println("Error getting album: ${e.message}")
            return null
        }

    }

    suspend fun getUser(userQuery: String): SpotifyPublicUser? {
        return api!!.users.getProfile(userQuery)
    }

    suspend fun findPlaylists(): ArrayList<PlaylistListItem>? {
        val playlists: ArrayList<PlaylistListItem>? = null

        try {
            val publicUser: SpotifyPublicUser? = getUser("Salmoe")
            api?.playlists?.getUserPlaylists(publicUser!!.id, 2)

            println("Success")
            return playlists
        } catch (e: Exception) {
            println("Error getting album: ${e.message}")
            return null
        }

    }

    suspend fun loadUrl(Url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(Url)
                val connection = url.openConnection()
                connection.connect()
                val inputStream = connection.getInputStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                bitmap
            }
            catch (e: Exception) {
                println("Error loading image: , ${e.message}")
                null
            }
        }
    }
}