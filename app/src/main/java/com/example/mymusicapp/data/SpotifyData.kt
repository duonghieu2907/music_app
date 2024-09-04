package com.example.mymusicapp.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.ContextUri
import com.adamratzman.spotify.models.SpotifyImage
import com.adamratzman.spotify.models.SpotifyPublicUser
import com.adamratzman.spotify.spotifyAppApi
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL


class SpotifyData {
    private val clientID = "724f1843a262436ca08ec70af0ae88fb"
    private val clientSecret = "209f1d3f5f6846148d92463a47378848"
    private val redirectUri = "https://www.facebook.com/"
    private var api: SpotifyAppApi? = null
    suspend fun buildSearchApi() {
        api = spotifyAppApi(clientID, clientSecret).build()
    }

    suspend fun findArtist(query: String) : Artist? {
        println("Successfully find artist")
        return api?.artists?.getArtist(query)
    }

    suspend fun findAlbumsFromArtist(query: String): ArrayList<Album>? {
        try {
            val albums = ArrayList<Album>()
            val simpleAlbum = api?.artists?.getArtistAlbums(query)
            for (i in 0..2) {
                albums.add(Album(simpleAlbum!![i].id, query,
                    simpleAlbum[i].name, simpleAlbum[i].releaseDate.toString(), checkImageReturnUrl(simpleAlbum[i].images)))
            }

            println("Success find albums from artist")
            return albums
        } catch (e: Exception) {
            println("Error getting album: ${e.message}")
            return null
        }

    }

    suspend fun findTracksFromAlbum(query: String): ArrayList<Track>? {
        val tracks = ArrayList<Track>()
        val album = api?.albums?.getAlbum(query)

        try {
            val rawTracks = api?.albums?.getAlbumTracks(query)
            for(i in 0..<rawTracks!!.size) {
                tracks.add(Track(rawTracks[i].id, album!!.id, rawTracks[i].name,
                    convertMilisecond(rawTracks[i].durationMs), rawTracks[i].uri.toString()))
            }
            return tracks
        }
        catch (e: Exception) {
            return null
        }

    }

    suspend fun getUserOnline(userQuery: String): SpotifyPublicUser? {
        println("Successfully find user")
        return api!!.users.getProfile(userQuery)
    }

    suspend fun startPlayback(uriOfArt_Alb_Pla: ContextUri, ) {

    }
    suspend fun findPlaylists(query: String): ArrayList<Playlist>? {
        val playlists = ArrayList<Playlist>()

        try {
            val rawPlaylists = api?.playlists?.getUserPlaylists(query)
            for(i in 0..<rawPlaylists!!.size) {
                playlists.add(Playlist(rawPlaylists[i].id, query, rawPlaylists[i].name, checkImageReturnUrl(rawPlaylists[i].images)))
            }

            println("Success")
            return playlists
        } catch (e: Exception) {
            println("Error getting album: ${e.message}")
            return null
        }

    }

    private suspend fun convertMilisecond(Ms: Int) : String {
        val totalSecond = Ms/1000 //Get total seconds
        val hours = totalSecond/3600
        val minutes = (totalSecond%3600)/60
        val seconds = totalSecond%60

        return when {
            hours > 0 -> String.format("%01d:%01d:%02d", hours, minutes, seconds)
            minutes > 0 -> String.format("%01d:%02d", minutes, seconds)
            else -> String.format("%02d", seconds)
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

    private fun checkImageReturnUrl(sample : List<SpotifyImage>) : String {
        return when {
            sample.isEmpty() -> ""
            else -> sample.firstOrNull()!!.url
        }
    }
}