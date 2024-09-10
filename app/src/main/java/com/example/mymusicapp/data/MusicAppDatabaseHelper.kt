package com.example.mymusicapp.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.mymusicapp.models.Album
import com.example.mymusicapp.models.Artist
import com.example.mymusicapp.models.Follower
import com.example.mymusicapp.models.Like
import com.example.mymusicapp.models.Playlist
import com.example.mymusicapp.models.PlaylistTrack
import com.example.mymusicapp.models.SearchResult
import com.example.mymusicapp.models.Track
import com.example.mymusicapp.models.User

class MusicAppDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "music_app1.db"
        private const val DATABASE_VERSION = 1

        // User Table
        private const val TABLE_USER = "User"
        private const val USER_ID = "user_id"
        private const val USER_NAME = "name"
        private const val USER_EMAIL = "email"
        private const val USER_PASSWORD = "password"
        private const val USER_DOB = "date_of_birth"
        private const val USER_PROFILE_IMAGE = "profile_image"

        // Artist Table
        private const val TABLE_ARTIST = "Artist"
        private const val ARTIST_ID = "artist_id"
        private const val ARTIST_NAME = "name"
        private const val ARTIST_GENRE = "genre"
        private const val ARTIST_IMAGE = "image"

        // Album Table
        private const val TABLE_ALBUM = "Album"
        private const val ALBUM_ID = "album_id"
        private const val ALBUM_ARTIST_ID = "artist_id"
        private const val ALBUM_NAME = "name"
        private const val ALBUM_RELEASE_DATE = "release_date"
        private const val ALBUM_IMAGE = "image"

        // Track Table
        private const val TABLE_TRACK = "Track"
        private const val TRACK_ID = "track_id"
        private const val TRACK_ALBUM_ID = "album_id"
        private const val TRACK_NAME = "name"
        private const val TRACK_DURATION = "duration"
        private const val TRACK_PATH = "path"

        // Playlist Table
        private const val TABLE_PLAYLIST = "Playlist"
        private const val PLAYLIST_ID = "playlist_id"
        private const val PLAYLIST_USER_ID = "user_id"
        private const val PLAYLIST_NAME = "name"
        private const val PLAYLIST_IMAGE = "image"


        // Playlist_Track Table
        private const val TABLE_PLAYLIST_TRACK = "Playlist_Track"
        private const val PLAYLIST_TRACK_PLAYLIST_ID = "playlist_id"
        private const val PLAYLIST_TRACK_TRACK_ID = "track_id"
        private const val PLAYLIST_TRACK_ORDER = "track_order"

        // Follower Table
        private const val TABLE_FOLLOWER = "Follower"
        private const val FOLLOWER_USER_ID = "user_id"
        private const val FOLLOWER_ARTIST_ID = "artist_id"

        // Like Table ??
        private const val TABLE_LIKE = "Like"
        private const val LIKE_USER_ID = "user_id"
        private const val LIKE_TRACK_ID = "track_id"

        // Follow playlist
        const val TABLE_FOLLOWED_PLAYLISTS = "FollowedPlaylists"
        const val FOLLOWED_USER_ID = "FollowedUserId"
        const val FOLLOWED_PLAYLIST_ID = "FollowedPlaylistId"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = ("CREATE TABLE $TABLE_USER ("
                + "$USER_ID TEXT PRIMARY KEY,"
                + "$USER_NAME TEXT,"
                + "$USER_EMAIL TEXT,"
                + "$USER_PASSWORD TEXT,"
                + "$USER_DOB TEXT,"
                + "$USER_PROFILE_IMAGE TEXT)")

        val createArtistTable = ("CREATE TABLE $TABLE_ARTIST ("
                + "$ARTIST_ID TEXT PRIMARY KEY,"
                + "$ARTIST_NAME TEXT,"
                + "$ARTIST_GENRE TEXT,"
                + "$ARTIST_IMAGE TEXT)")

        val createAlbumTable = ("CREATE TABLE $TABLE_ALBUM ("
                + "$ALBUM_ID TEXT PRIMARY KEY,"
                + "$ALBUM_ARTIST_ID TEXT,"
                + "$ALBUM_NAME TEXT,"
                + "$ALBUM_RELEASE_DATE TEXT,"
                + "$ALBUM_IMAGE TEXT,"
                + "FOREIGN KEY($ALBUM_ARTIST_ID) REFERENCES $TABLE_ARTIST($ARTIST_ID))")

        val createTrackTable = ("CREATE TABLE $TABLE_TRACK ("
                + "$TRACK_ID TEXT PRIMARY KEY,"
                + "$TRACK_ALBUM_ID TEXT,"
                + "$TRACK_NAME TEXT,"
                + "$TRACK_DURATION TEXT,"
                + "$TRACK_PATH TEXT,"
                + "FOREIGN KEY($TRACK_ALBUM_ID) REFERENCES $TABLE_ALBUM($ALBUM_ID))")

        val createPlaylistTable = ("CREATE TABLE $TABLE_PLAYLIST ("
                + "$PLAYLIST_ID TEXT PRIMARY KEY,"
                + "$PLAYLIST_USER_ID TEXT,"
                + "$PLAYLIST_NAME TEXT,"
                + "$PLAYLIST_IMAGE TEXT,"
                + "FOREIGN KEY($PLAYLIST_USER_ID) REFERENCES $TABLE_USER($USER_ID))")

        val createPlaylistTrackTable = ("CREATE TABLE $TABLE_PLAYLIST_TRACK ("
                + "$PLAYLIST_TRACK_PLAYLIST_ID TEXT,"
                + "$PLAYLIST_TRACK_TRACK_ID TEXT,"
                + "$PLAYLIST_TRACK_ORDER INTEGER,"
                + "PRIMARY KEY($PLAYLIST_TRACK_PLAYLIST_ID, $PLAYLIST_TRACK_TRACK_ID),"
                + "FOREIGN KEY($PLAYLIST_TRACK_PLAYLIST_ID) REFERENCES $TABLE_PLAYLIST($PLAYLIST_ID),"
                + "FOREIGN KEY($PLAYLIST_TRACK_TRACK_ID) REFERENCES $TABLE_TRACK($TRACK_ID))")

        val createFollowerTable = ("CREATE TABLE $TABLE_FOLLOWER ("
                + "$FOLLOWER_USER_ID TEXT,"
                + "$FOLLOWER_ARTIST_ID TEXT,"
                + "PRIMARY KEY($FOLLOWER_USER_ID, $FOLLOWER_ARTIST_ID),"
                + "FOREIGN KEY($FOLLOWER_USER_ID) REFERENCES $TABLE_USER($USER_ID),"
                + "FOREIGN KEY($FOLLOWER_ARTIST_ID) REFERENCES $TABLE_ARTIST($ARTIST_ID))")

        val createLikeTable = ("CREATE TABLE $TABLE_LIKE ("
                + "$LIKE_USER_ID TEXT,"
                + "$LIKE_TRACK_ID TEXT,"
                + "PRIMARY KEY($LIKE_USER_ID, $LIKE_TRACK_ID),"
                + "FOREIGN KEY($LIKE_USER_ID) REFERENCES $TABLE_USER($USER_ID),"
                + "FOREIGN KEY($LIKE_TRACK_ID) REFERENCES $TABLE_TRACK($TRACK_ID))")

        // New table for users following playlists
        val createFollowedPlaylistsTable = ("CREATE TABLE $TABLE_FOLLOWED_PLAYLISTS ("
                + "$FOLLOWED_USER_ID TEXT,"
                + "$FOLLOWED_PLAYLIST_ID TEXT,"
                + "PRIMARY KEY($FOLLOWED_USER_ID, $FOLLOWED_PLAYLIST_ID),"
                + "FOREIGN KEY($FOLLOWED_USER_ID) REFERENCES $TABLE_USER($USER_ID),"
                + "FOREIGN KEY($FOLLOWED_PLAYLIST_ID) REFERENCES $TABLE_PLAYLIST($PLAYLIST_ID))")

        // Execute the SQL statements
        db.execSQL(createUserTable)
        db.execSQL(createArtistTable)
        db.execSQL(createAlbumTable)
        db.execSQL(createTrackTable)
        db.execSQL(createPlaylistTable)
        db.execSQL(createPlaylistTrackTable)
        db.execSQL(createFollowerTable) //follow artist
        db.execSQL(createLikeTable) //like track
        db.execSQL(createFollowedPlaylistsTable)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS \"$TABLE_LIKE\"")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FOLLOWER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLAYLIST_TRACK")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLAYLIST")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRACK")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ALBUM")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ARTIST")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    private fun deleteAllData(tableName: String, db: SQLiteDatabase) {
        val deleteQuery = "DELETE FROM $tableName"
        db.execSQL(deleteQuery)

        Log.d("DATABASE", "Successfully delete data")
    }

    // CRUD Operations for Users
    fun addUser(user: User): String {
        val db = this.writableDatabase
        val query : String = "SELECT 1 FROM $TABLE_USER WHERE $USER_ID = ?"

        //Make sure that user does exist
        val cursor = db.rawQuery(query, arrayOf(user.userId))
        if(cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return ""
        }
        cursor.close()

        //Add a new values
        val values = ContentValues().apply {
            put(USER_ID, user.userId)
            put(USER_NAME, user.name)
            put(USER_EMAIL, user.email)
            put(USER_PASSWORD, user.password)
            put(USER_DOB, user.dateOfBirth)
            put(USER_PROFILE_IMAGE, user.profileImage)
        }

        // Insert the new user and return the ID
        db.insert(TABLE_USER, null, values)
        db.close()

        return user.userId
    }


    fun getUser(userId: String): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USER,
            arrayOf(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD, USER_DOB, USER_PROFILE_IMAGE),
            "$USER_ID=?",
            arrayOf(userId),
            null,
            null,
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val user = User(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5)
            )
            cursor.close()
            user
        } else {
            cursor?.close()
            null
        }
    }



    // CRUD Operations for Artist
    fun addArtist(artist: Artist): String {
        val db = this.writableDatabase
        val query : String = "SELECT 1 FROM $TABLE_ARTIST WHERE $ARTIST_ID = ?"

        //Make sure that album does exist
        val cursor = db.rawQuery(query, arrayOf(artist.artistId))
        if(cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return ""
        }
        cursor.close()

        val values = ContentValues().apply {
            put(ARTIST_ID, artist.artistId)
            put(ARTIST_NAME, artist.name)
            put(ARTIST_GENRE, artist.genre)
            put(ARTIST_IMAGE, artist.image)
        }

        // Insert the new artist and return the artist ID
        db.insert(TABLE_ARTIST, null, values)
        db.close()

        return artist.artistId
    }

    fun getAllArtistId() : ArrayList<String>? {
        val db = this.readableDatabase
        val query = "SELECT DISTINCT $ARTIST_ID FROM $TABLE_ARTIST"
        val cursor = db.rawQuery(query, arrayOf())
        val allId = ArrayList<String>()
        val columnIndex = cursor.getColumnIndexOrThrow(ARTIST_ID)
        var counter = 0

        if(!cursor.moveToFirst()) return null //Check if there is no album

        do {
            allId.add(cursor.getString(columnIndex))
            counter++
        } while (cursor.moveToNext())

        return allId
    }

    fun getArtist(artistId: String): Artist? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_ARTIST,
            arrayOf(ARTIST_ID, ARTIST_NAME, ARTIST_GENRE, ARTIST_IMAGE),
            "$ARTIST_ID=?",
            arrayOf(artistId),
            null,
            null,
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val artist = Artist(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
            )
            cursor.close()
            artist
        } else {
            cursor?.close()
            null
        }
    }



    fun updateArtist(artist: Artist) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(ARTIST_NAME, artist.name)
            put(ARTIST_GENRE, artist.genre)
            put(ARTIST_IMAGE, artist.image)
        }
        db.update(TABLE_ARTIST, values, "$ARTIST_ID=?", arrayOf(artist.artistId.toString()))
        db.close()
    }

    fun deleteArtist(artistId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_ARTIST, "$ARTIST_ID=?", arrayOf(artistId.toString()))
        db.close()
    }


    // CRUD Operations for Albums
    fun addAlbum(album: Album): String {
        val db = this.writableDatabase
        val query : String = "SELECT $ALBUM_ID FROM $TABLE_ALBUM WHERE $ALBUM_ID = ?"

        //Make sure that album does exist
        val cursor = db.rawQuery(query, arrayOf(album.albumId))
        if(cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return ""
        }
        cursor.close()

        val values = ContentValues().apply {
            put(ALBUM_ID, album.albumId)
            put(ALBUM_ARTIST_ID, album.artistId)
            put(ALBUM_NAME, album.name)
            put(ALBUM_RELEASE_DATE, album.releaseDate)
            put(ALBUM_IMAGE, album.image)
        }

        // Insert the new album and return the album ID
        db.insert(TABLE_ALBUM, null, values)
        db.close()

        return album.albumId
    }

    fun getAllAlbumId() : ArrayList<String>? {
        val db = this.readableDatabase
        val query = "SELECT DISTINCT $ALBUM_ID FROM $TABLE_ALBUM"
        val cursor = db.rawQuery(query, arrayOf())
        val allId = ArrayList<String>()
        val columnIndex = cursor.getColumnIndexOrThrow(ALBUM_ID)
        var counter = 0

        if(!cursor.moveToFirst()) return null //Check if there is no album

        do {
            allId.add(cursor.getString(columnIndex))
            counter++
        } while (cursor.moveToNext())

        return allId
    }

    fun getAlbum(albumId: String): Album? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_ALBUM,
            arrayOf(ALBUM_ID, ALBUM_ARTIST_ID, ALBUM_NAME, ALBUM_RELEASE_DATE, ALBUM_IMAGE),
            "$ALBUM_ID=?",
            arrayOf(albumId.toString()),
            null,
            null,
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val album = Album(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
            )
            cursor.close()
            album
        } else {
            cursor?.close()
            null
        }
    }

    fun updateAlbum(album: Album) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(ALBUM_ARTIST_ID, album.artistId)
            put(ALBUM_NAME, album.name)
            put(ALBUM_RELEASE_DATE, album.releaseDate)
            put(ALBUM_IMAGE, album.image)
        }
        db.update(TABLE_ALBUM, values, "$ALBUM_ID=?", arrayOf(album.albumId.toString()))
        db.close()
    }

    fun deleteAlbum(albumId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_ALBUM, "$ALBUM_ID=?", arrayOf(albumId.toString()))
        db.close()
    }


    // CRUD Operations for Tracks
    fun addTrack(track: Track): String {
        val db = this.writableDatabase
        val query : String = "SELECT 1 FROM $TABLE_TRACK WHERE $TRACK_ID = ?"

        //Make sure that track does exist
        val cursor = db.rawQuery(query, arrayOf(track.trackId))
        if(cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return ""
        }
        cursor.close()

        val values = ContentValues().apply {
            put(TRACK_ID, track.trackId)
            put(TRACK_ALBUM_ID, track.albumId)
            put(TRACK_NAME, track.name)
            put(TRACK_DURATION, track.duration)
            put(TRACK_PATH, track.path)
        }

        db.insert("Track", null, values)
        db.close()

        return track.trackId
    }


    fun getTrack(trackId: Int): Track? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_TRACK,
            arrayOf(TRACK_ID, TRACK_ALBUM_ID, TRACK_NAME, TRACK_DURATION, TRACK_PATH),
            "$TRACK_ID=?",
            arrayOf(trackId.toString()),
            null,
            null,
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val track = Track(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
            )
            cursor.close()
            track
        } else {
            cursor?.close()
            null
        }
    }

    fun updateTrack(track: Track) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(TRACK_ALBUM_ID, track.albumId)
            put(TRACK_NAME, track.name)
            put(TRACK_DURATION, track.duration)
            put(TRACK_PATH, track.path)
        }
        db.update(TABLE_TRACK, values, "$TRACK_ID=?", arrayOf(track.trackId.toString()))
        db.close()
    }

    fun deleteTrack(trackId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_TRACK, "$TRACK_ID=?", arrayOf(trackId.toString()))
        db.close()
    }



    // CRUD Operations for Playlists
    fun addPlaylist(playlist: Playlist): String {
        val db = this.writableDatabase
        val query : String = "SELECT 1 FROM $TABLE_PLAYLIST WHERE $PLAYLIST_ID = ?"

        //Make sure that playlist does exist
        val cursor = db.rawQuery(query, arrayOf(playlist.playlistId))
        if(cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return ""
        }
        cursor.close()

        val values = ContentValues().apply {
            put(PLAYLIST_ID, playlist.playlistId)
            put(PLAYLIST_USER_ID, playlist.userId)
            put(PLAYLIST_NAME, playlist.name)
            put(PLAYLIST_IMAGE, playlist.image)
        }

        // Insert the new playlist and return the playlist name
        db.insert(TABLE_PLAYLIST, null, values)
        db.close()

        return playlist.playlistId
    }

    fun getAllPlaylistId() : ArrayList<String>? {
        val db = this.readableDatabase
        val query = "SELECT DISTINCT $PLAYLIST_ID FROM $TABLE_PLAYLIST"
        val cursor = db.rawQuery(query, arrayOf())
        val allId = ArrayList<String>()
        val columnIndex = cursor.getColumnIndexOrThrow(PLAYLIST_ID)
        var counter = 0

        if(!cursor.moveToFirst()) return null //Check if there is no album

        do {
            allId.add(cursor.getString(columnIndex))
            counter++
        } while (cursor.moveToNext())

        return allId
    }

    fun getPlaylist(playlistId: String): Playlist? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PLAYLIST,
            arrayOf(PLAYLIST_ID, PLAYLIST_USER_ID, PLAYLIST_NAME, PLAYLIST_IMAGE),
            "$PLAYLIST_ID=?",
            arrayOf(playlistId.toString()),
            null,
            null,
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val playlist = Playlist(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
            )
            cursor.close()
            playlist
        } else {
            cursor?.close()
            null
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(PLAYLIST_USER_ID, playlist.userId)
            put(PLAYLIST_NAME, playlist.name)
            put(PLAYLIST_IMAGE, playlist.image)
        }
        db.update(TABLE_PLAYLIST, values, "$PLAYLIST_ID=?", arrayOf(playlist.playlistId.toString()))
        db.close()
    }

    fun deletePlaylist(playlistId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_PLAYLIST, "$PLAYLIST_ID=?", arrayOf(playlistId.toString()))
        db.close()
    }

    //LikedSong
    fun addUserLikedSongsPlaylist(userId: String) {
        val db = this.writableDatabase
        val playlistId = "userLikedSong"  // Unique ID for the liked songs playlist
        val playlistName = "Liked Songs"

        val values = ContentValues().apply {
            put(PLAYLIST_ID, playlistId)
            put(PLAYLIST_USER_ID, userId)
            put(PLAYLIST_NAME, playlistName)
            put(PLAYLIST_IMAGE, "") // Image can be set later
        }

        db.insert(TABLE_PLAYLIST, null, values)
        db.close()
    }

    fun isTrackLiked(trackId: String, playlistId: String = "userLikedSong"): Boolean {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        return try {
            // Query to check if the track exists in the specified playlist
            cursor = db.query(
                TABLE_PLAYLIST_TRACK,  // Table name
                arrayOf(PLAYLIST_TRACK_TRACK_ID),  // Columns to return
                "$PLAYLIST_TRACK_TRACK_ID = ? AND $PLAYLIST_TRACK_PLAYLIST_ID = ?",  // WHERE clause
                arrayOf(trackId, playlistId),  // Arguments for WHERE clause
                null,  // GROUP BY
                null,  // HAVING
                null   // ORDER BY
            )

            val isLiked = cursor.moveToFirst()  // If cursor has at least one row, track is liked
            cursor.close()
            db.close()
            isLiked
        } catch (e: SQLiteException) {
            e.printStackTrace()
            cursor?.close()
            db.close()
            false
        }
    }



    // CRUD Operations for Playlist_Tracks
    fun addPlaylistTrack(playlistTrack: PlaylistTrack) {
        val db = this.writableDatabase
        val query : String = "SELECT 1 FROM $TABLE_PLAYLIST_TRACK WHERE $PLAYLIST_TRACK_PLAYLIST_ID = ?"

        //Make sure that playlist track does exist
        val cursor = db.rawQuery(query, arrayOf(playlistTrack.playlistId))
        if(cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return
        }
        cursor.close()

        val values = ContentValues().apply {
            put(PLAYLIST_TRACK_PLAYLIST_ID, playlistTrack.playlistId)
            put(PLAYLIST_TRACK_TRACK_ID, playlistTrack.trackId)
            put(PLAYLIST_TRACK_ORDER, playlistTrack.trackOrder)
        }
        db.insert(TABLE_PLAYLIST_TRACK, null, values)
        db.close()
    }
    fun addPlaylistTrack(playlistId: String, trackId: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(PLAYLIST_TRACK_PLAYLIST_ID, playlistId)
            put(PLAYLIST_TRACK_TRACK_ID, trackId)
        }

        return try {
            val result = db.insert(TABLE_PLAYLIST_TRACK, null, contentValues)
            db.close()
            result != -1L  // If result is -1, the insertion failed
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }


    fun getPlaylistTrack(playlistId: Int, trackId: Int): PlaylistTrack? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PLAYLIST_TRACK,
            arrayOf(PLAYLIST_TRACK_PLAYLIST_ID, PLAYLIST_TRACK_TRACK_ID, PLAYLIST_TRACK_ORDER),
            "$PLAYLIST_TRACK_PLAYLIST_ID=? AND $PLAYLIST_TRACK_TRACK_ID=?",
            arrayOf(playlistId.toString(), trackId.toString()),
            null,
            null,
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val playlistTrack = PlaylistTrack(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getInt(2)
            )
            cursor.close()
            playlistTrack
        } else {
            cursor?.close()
            null
        }
    }

    fun updatePlaylistTrack(playlistTrack: PlaylistTrack) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(PLAYLIST_TRACK_ORDER, playlistTrack.trackOrder)
        }
        db.update(
            TABLE_PLAYLIST_TRACK,
            values,
            "$PLAYLIST_TRACK_PLAYLIST_ID=? AND $PLAYLIST_TRACK_TRACK_ID=?",
            arrayOf(playlistTrack.playlistId.toString(), playlistTrack.trackId.toString())
        )
        db.close()
    }

    fun deletePlaylistTrack(playlistId: String, trackId: String): Boolean {
        val db = this.writableDatabase

        return try {
            val result = db.delete(
                TABLE_PLAYLIST_TRACK,
                "$PLAYLIST_TRACK_PLAYLIST_ID = ? AND $PLAYLIST_TRACK_TRACK_ID = ?",
                arrayOf(playlistId, trackId)
            )
            db.close()
            result > 0  // deletion was successful
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }



    ///delete all

    fun deleteAll() {
        val db: SQLiteDatabase = this.writableDatabase
        try {
            db.beginTransaction()
            deleteAllData(TABLE_PLAYLIST_TRACK, db)
            deleteAllData(TABLE_ALBUM, db)
            deleteAllData(TABLE_PLAYLIST, db)
            deleteAllData(TABLE_USER, db)
            deleteAllData(TABLE_ARTIST, db)
            deleteAllData(TABLE_TRACK, db)
            deleteAllData(TABLE_FOLLOWER, db)
            deleteAllData(TABLE_LIKE, db)

            db.setTransactionSuccessful()
        }
        catch (e: Exception) {
            Log.e("MusicAppDatabaseHelper", "Error deleting data")
        }
        finally {
            db.endTransaction()
        }

        db.close()
    }


    // CRUD Operations for Followers
    fun addFollower(follower: Follower) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(FOLLOWER_USER_ID, follower.userId)
            put(FOLLOWER_ARTIST_ID, follower.artistId)
        }
        db.insert(TABLE_FOLLOWER, null, values)
        db.close()
    }

    fun getFollower(userId: Int, artistId: Int): Follower? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_FOLLOWER,
            arrayOf(FOLLOWER_USER_ID, FOLLOWER_ARTIST_ID),
            "$FOLLOWER_USER_ID=? AND $FOLLOWER_ARTIST_ID=?",
            arrayOf(userId.toString(), artistId.toString()),
            null,
            null,
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val follower = Follower(
                cursor.getString(0),
                cursor.getString(1)
            )
            cursor.close()
            follower
        } else {
            cursor?.close()
            null
        }
    }

    fun deleteFollower(userId: Int, artistId: Int) {
        val db = this.writableDatabase
        db.delete(
            TABLE_FOLLOWER,
            "$FOLLOWER_USER_ID=? AND $FOLLOWER_ARTIST_ID=?",
            arrayOf(userId.toString(), artistId.toString())
        )
        db.close()
    }


    // CRUD Operations for Likes
    fun addLike(like: Like) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(LIKE_USER_ID, like.userId)
            put(LIKE_TRACK_ID, like.trackId)
        }
        db.insert(TABLE_LIKE, null, values)
        db.close()
    }

    fun getLike(userId: Int, trackId: Int): Like? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_LIKE,
            arrayOf(LIKE_USER_ID, LIKE_TRACK_ID),
            "$LIKE_USER_ID=? AND $LIKE_TRACK_ID=?",
            arrayOf(userId.toString(), trackId.toString()),
            null,
            null,
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val like = Like(
                cursor.getString(0),
                cursor.getString(1)
            )
            cursor.close()
            like
        } else {
            cursor?.close()
            null
        }
    }

    fun deleteLike(userId: Int, trackId: Int) {
        val db = this.writableDatabase
        db.delete(
            TABLE_LIKE,
            "$LIKE_USER_ID=? AND $LIKE_TRACK_ID=?",
            arrayOf(userId.toString(), trackId.toString())
        )
        db.close()
    }

    //followed playlist

    fun followPlaylist(userId: String, playlistId: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(FOLLOWED_USER_ID, userId)
            put(FOLLOWED_PLAYLIST_ID, playlistId)
        }
        db.insert(TABLE_FOLLOWED_PLAYLISTS, null, values)
        db.close()
    }

    fun unfollowPlaylist(userId: String, playlistId: String) {
        val db = this.writableDatabase
        db.delete(TABLE_FOLLOWED_PLAYLISTS, "$FOLLOWED_USER_ID = ? AND $FOLLOWED_PLAYLIST_ID = ?", arrayOf(userId, playlistId))
        db.close()
    }

    @SuppressLint("Range")
    fun getUserLibraryPlaylists(userId: String): List<Playlist> {
        val playlists = mutableListOf<Playlist>()
        val db = this.readableDatabase

        // Query for playlists created by the user
        val userPlaylistsCursor = db.query(
            TABLE_PLAYLIST,
            null,
            "$PLAYLIST_USER_ID = ?",
            arrayOf(userId),
            null,
            null,
            null
        )

        while (userPlaylistsCursor.moveToNext()) {
            // Extract playlist details and add to the list
            val playlist = Playlist(
                userPlaylistsCursor.getString(userPlaylistsCursor.getColumnIndex(PLAYLIST_ID)),
                userPlaylistsCursor.getString(userPlaylistsCursor.getColumnIndex(PLAYLIST_NAME)),
                userPlaylistsCursor.getString(userPlaylistsCursor.getColumnIndex(PLAYLIST_IMAGE)),
                userId
            )
            playlists.add(playlist)
        }
        userPlaylistsCursor.close()

        // Query for playlists followed by the user
        val followedPlaylistsCursor = db.rawQuery(
            "SELECT * FROM $TABLE_PLAYLIST WHERE $PLAYLIST_ID IN (SELECT $FOLLOWED_PLAYLIST_ID FROM $TABLE_FOLLOWED_PLAYLISTS WHERE $FOLLOWED_USER_ID = ?)",
            arrayOf(userId)
        )

        while (followedPlaylistsCursor.moveToNext()) {
            // Extract playlist details and add to the list
            val playlist = Playlist(
                followedPlaylistsCursor.getString(followedPlaylistsCursor.getColumnIndex(PLAYLIST_ID)),
                followedPlaylistsCursor.getString(followedPlaylistsCursor.getColumnIndex(PLAYLIST_NAME)),
                followedPlaylistsCursor.getString(followedPlaylistsCursor.getColumnIndex(PLAYLIST_IMAGE)),
                followedPlaylistsCursor.getString(followedPlaylistsCursor.getColumnIndex(PLAYLIST_USER_ID))
            )
            playlists.add(playlist)
        }
        followedPlaylistsCursor.close()

        db.close()
        return playlists
    }


    fun getTracksByPlaylistId(playlistId: String): List<Track> {
        val tracks = mutableListOf<Track>()
        val db = this.readableDatabase

        // Query to get tracks that are part of the specified playlist
        val query = """
        SELECT t.$TRACK_ID, t.$TRACK_ALBUM_ID, t.$TRACK_NAME, t.$TRACK_DURATION, t.$TRACK_PATH
        FROM $TABLE_TRACK t
        INNER JOIN $TABLE_PLAYLIST_TRACK pt ON t.$TRACK_ID = pt.$PLAYLIST_TRACK_TRACK_ID
        WHERE pt.$PLAYLIST_TRACK_PLAYLIST_ID = ?
        ORDER BY pt.$PLAYLIST_TRACK_ORDER ASC
    """
        val cursor = db.rawQuery(query, arrayOf(playlistId.toString()))

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val trackId = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_ID))
                val albumId = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_ALBUM_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_NAME))
                val duration = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_DURATION))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_PATH))
                println("Success: " + playlistId + " " + name)
                // Create a Track object
                val track = Track(
                    trackId = trackId,
                    albumId = albumId,
                    name = name,
                    duration = duration,
                    path = path
                )

                tracks.add(track) // Add the track to the list

            } while (cursor.moveToNext()) // Move to the next result
        }

        cursor?.close() // Close the cursor after use

        return tracks // Return the list of tracks
    }

    fun getTracksByAlbumId(albumId: String): List<Track> {
        val tracks = mutableListOf<Track>()
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_TRACK WHERE $TRACK_ALBUM_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(albumId))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_NAME))
                val duration = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_DURATION))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_PATH))

                val track = Track(id, albumId, name, duration, path)
                tracks.add(track)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return tracks
    }


    fun search(keyword: String): List<SearchResult> {
        val db = this.readableDatabase
        val searchResults = mutableListOf<SearchResult>()
        val searchQuery = "$keyword%"

        // Search in Track table with Album image
        val trackCursor = db.rawQuery(
            "SELECT t.$TRACK_ID, t.$TRACK_NAME, a.$ALBUM_IMAGE, a.$ALBUM_NAME " +
                    "FROM $TABLE_TRACK t " +
                    "LEFT JOIN $TABLE_ALBUM a ON t.$TRACK_ALBUM_ID = a.$ALBUM_ID " +
                    "WHERE t.$TRACK_NAME LIKE ?",
            arrayOf(searchQuery)
        )
        if (trackCursor.moveToFirst()) {
            do {
                searchResults.add(
                    SearchResult(
                        id = trackCursor.getString(0),
                        name = trackCursor.getString(1),
                        type = "Track",
                        image = trackCursor.getString(2),  // Album image
                        subName = trackCursor.getString(3) // Album name
                    )
                )
            } while (trackCursor.moveToNext())
        }
        trackCursor.close()

        // Search in Artist table
        val artistCursor = db.query(
            TABLE_ARTIST,
            arrayOf(ARTIST_ID, ARTIST_NAME, ARTIST_IMAGE),
            "$ARTIST_NAME LIKE ?",
            arrayOf(searchQuery),
            null,
            null,
            null
        )
        if (artistCursor.moveToFirst()) {
            do {
                searchResults.add(
                    SearchResult(
                        id = artistCursor.getString(0),
                        name = artistCursor.getString(1),
                        type = "Artist",
                        image = artistCursor.getString(2),
                        subName = null // No subName for artist
                    )
                )
            } while (artistCursor.moveToNext())
        }
        artistCursor.close()

        // Search in Album table with Artist name
        val albumCursor = db.rawQuery(
            "SELECT a.$ALBUM_ID, a.$ALBUM_NAME, a.$ALBUM_IMAGE, ar.$ARTIST_NAME " +
                    "FROM $TABLE_ALBUM a " +
                    "LEFT JOIN $TABLE_ARTIST ar ON a.$ALBUM_ARTIST_ID = ar.$ARTIST_ID " +
                    "WHERE a.$ALBUM_NAME LIKE ?",
            arrayOf(searchQuery)
        )
        if (albumCursor.moveToFirst()) {
            do {
                searchResults.add(
                    SearchResult(
                        id = albumCursor.getString(0),
                        name = albumCursor.getString(1),
                        type = "Album",
                        image = albumCursor.getString(2),
                        subName = albumCursor.getString(3) // Artist name
                    )
                )
            } while (albumCursor.moveToNext())
        }
        albumCursor.close()

        // Search in Playlist table with Owner's name
        val playlistCursor = db.rawQuery(
            "SELECT p.$PLAYLIST_ID, p.$PLAYLIST_NAME, p.$PLAYLIST_IMAGE, u.$USER_NAME " +
                    "FROM $TABLE_PLAYLIST p " +
                    "LEFT JOIN $TABLE_USER u ON p.$PLAYLIST_USER_ID = u.$USER_ID " +
                    "WHERE p.$PLAYLIST_NAME LIKE ?",
            arrayOf(searchQuery)
        )
        if (playlistCursor.moveToFirst()) {
            do {
                searchResults.add(
                    SearchResult(
                        id = playlistCursor.getString(0),
                        name = playlistCursor.getString(1),
                        type = "Playlist",
                        image = playlistCursor.getString(2),
                        subName = playlistCursor.getString(3) // Owner's name
                    )
                )
            } while (playlistCursor.moveToNext())
        }
        playlistCursor.close()

        db.close()
        return searchResults
    }


}

