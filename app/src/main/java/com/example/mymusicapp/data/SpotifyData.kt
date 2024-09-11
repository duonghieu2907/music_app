package com.example.mymusicapp.data

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.SpotifyUserAuthorization
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.SpotifyImage
import com.adamratzman.spotify.models.SpotifyPublicUser
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.spotifyClientApi
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.random.Random


class SpotifyData {
    private val clientID = "724f1843a262436ca08ec70af0ae88fb"
    private val clientSecret = "209f1d3f5f6846148d92463a47378848"
    private val redirectUri = "spotifyapp://callback"
    private val userId = "31j3r46xev5snbqwjqbuwbrm6cwu"
    private var clientApi: SpotifyClientApi? = null
    private var api: SpotifyAppApi? = null
    private val AUTH_REQUEST_CODE = 1001
    private fun generateRandomString(length: Int): String {
        val random = Random
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val result = StringBuilder(length)
        for(i in 0..length) {
            result.append(chars[random.nextInt(chars.length)])
        }
        return result.toString()
    }
    suspend fun buildAuthcode(activity: Activity) {
        var state = generateRandomString(16)
        val builder = spotifyClientApi(clientID, clientSecret, redirectUri)
        val authorizationUri = builder.getAuthorizationUrl(
            SpotifyScope.USER_READ_PLAYBACK_STATE,
            SpotifyScope.USER_MODIFY_PLAYBACK_STATE,
            SpotifyScope.USER_READ_CURRENTLY_PLAYING,
            SpotifyScope.APP_REMOTE_CONTROL,
            SpotifyScope.STREAMING, state = state)

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authorizationUri))
        activity.startActivityForResult(intent, AUTH_REQUEST_CODE)
        Log.d("BuildAuthCode", "Redirecting")
    }

    suspend fun buildClientApi(authCode: String) {
        clientApi = spotifyClientApi(clientID, clientSecret, redirectUri)
                .authorization(SpotifyUserAuthorization(authorizationCode = authCode))
                .build()
        Log.d("buildClientApi", "Success")
    }

    suspend fun buildAppApi() {
        api = spotifyAppApi(clientID, clientSecret).build()
        Log.d("buildClientApi", "Success")
    }

    suspend fun findArtist(query: String) : Artist? {
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

    suspend fun findAlbumFromTracks(query: String): com.adamratzman.spotify.models.Album? {
        return api?.albums?.getAlbum(query)
    }

    suspend fun getUserOnline(userQuery: String): SpotifyPublicUser? {
        println("Successfully find user")
        return api!!.users.getProfile(userQuery)
    }

    suspend fun startPlayback() {
        //use clientApi for playback permission
    }
    suspend fun findPlaylistsFromUser(query: String): ArrayList<Playlist>? {
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

    suspend fun findTracksFromPlaylist(query: String): ArrayList<Track>? {
        val tracks = ArrayList<Track>()

        try {
            val rawTracks = api?.playlists?.getPlaylistTracks(query)?:return null

            for(i in 0..<rawTracks.size) {
                val temp = api?.tracks?.getTrack(rawTracks[i].track?.id!!)
                tracks.add(Track(rawTracks[i].track?.id!!, rawTracks[i].track?.asTrack!!.album.id,
                    rawTracks[i].track?.asTrack!!.name, convertMilisecond(rawTracks[i].track?.asTrack!!.durationMs),
                    rawTracks[i].track?.asTrack!!.uri.toString()))
            }

            println("Success")
            return tracks
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