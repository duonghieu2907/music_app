package com.example.mymusicapp.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.spotifyAppApi
import com.example.mymusicapp.library.AlbumItem
import java.net.URL

class SpotifyData {
    private val clientID = "95347603f19a497aa51c78b2a3dd81d3"
    private val clientSecret = "ca5f8fa71e8542a98035ded2caa67efa"
    private var api: SpotifyAppApi? = null
    suspend fun buildSearchApi() {
        api = spotifyAppApi(clientID, clientSecret).build()
    }

    suspend fun findAllPlaylist(): ArrayList<AlbumItem>? {
        val albums: ArrayList<AlbumItem>? = null
        buildSearchApi()

        try {
            val temp =api?.albums?.getAlbum("3iPSVi54hsacKKl1xIR2eH")
            albums?.add(AlbumItem(temp!!.name,
                urlToBitmap(temp.images[0].url, temp.images[0].height, temp.images[0].width)))

            println("Success")
            return albums
        }
        catch (e: Exception) {
            println("Error getting album: ${e.message}")
            return null
        }

    }

    fun urlToBitmap(urlString: String, targetWidth: Int?, targetHeight: Int?): Bitmap? {
        return try {
            val url = URL(urlString)
            val connection = url.openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true  // Get image dimensions only
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close() // Close the stream

            // Calculate inSampleSize for scaling
            val inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)
            options.inJustDecodeBounds = false
            options.inSampleSize = inSampleSize

            // Decode the image with the calculated inSampleSize
            val inputStream2 = connection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream2, null, options)
            inputStream2.close()

            return bitmap
        } catch (e: Exception) {
            // Handle exceptions (e.g., invalid URL, network errors)
            println("Error decoding image: ${e.message}")
            null
        }
    }

    // Helper function to calculate inSampleSize
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int?, reqHeight: Int?): Int {
        var inSampleSize = 1
        val height = options.outHeight
        val width = options.outWidth

        if (height > reqHeight!! || width > reqWidth!!) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth!!) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

}