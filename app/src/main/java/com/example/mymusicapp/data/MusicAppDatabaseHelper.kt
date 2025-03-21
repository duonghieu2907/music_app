package com.example.mymusicapp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
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
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

class MusicAppDatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "music_app1.db"
        private const val DATABASE_VERSION = 5

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
        private const val ARTIST_TIMESTAMP = "added_at"

        // Album Table
        private const val TABLE_ALBUM = "Album"
        private const val ALBUM_ID = "album_id"
        private const val ALBUM_ARTIST_ID = "artist_id"
        private const val ALBUM_NAME = "name"
        private const val ALBUM_RELEASE_DATE = "release_date"
        private const val ALBUM_IMAGE = "image"
        private const val ALBUM_TIMESTAMP = "added_at"

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
        private const val PLAYLIST_TIMESTAMP = "added_at"


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
        const val FOLLOWED_PLAYLIST_USER_ID = "FollowedUserId"
        const val FOLLOWED_PLAYLIST_ID = "FollowedPlaylistId"

        // Liked Albums
        const val TABLE_FOLLOWED_ALBUMS = "FollowedAlbums"
        const val FOLLOWED_ALBUM_USER_ID = "FollowedAlbumUserId"
        const val FOLLOWED_ALBUM_ID = "FollowedAlbumId"

        //History
        const val TABLE_HISTORY = "History"
        const val HISTORY_USER_ID = "user_id"
        const val HISTORY_PLAYLIST_ID = "playlist_id"
        const val HISTORY_TRACK_ID = "track_id"
        const val HISTORY_TIMESTAMP = "listen_at"
    }

    /*override fun onCreate(db: SQLiteDatabase) {
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
                + "$ARTIST_IMAGE TEXT,"
                + "$ARTIST_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP)")

        val createAlbumTable = ("CREATE TABLE $TABLE_ALBUM ("
                + "$ALBUM_ID TEXT PRIMARY KEY,"
                + "$ALBUM_ARTIST_ID TEXT,"
                + "$ALBUM_NAME TEXT,"
                + "$ALBUM_RELEASE_DATE TEXT,"
                + "$ALBUM_IMAGE TEXT,"
                + "$ALBUM_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP,"
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
                + "$PLAYLIST_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP,"
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

        // New table for users following playlists, albums
        val createFollowedPlaylistsTable = ("CREATE TABLE $TABLE_FOLLOWED_PLAYLISTS ("
                + "$FOLLOWED_PLAYLIST_USER_ID TEXT,"
                + "$FOLLOWED_PLAYLIST_ID TEXT,"
                + "PRIMARY KEY($FOLLOWED_PLAYLIST_USER_ID, $FOLLOWED_PLAYLIST_ID),"
                + "FOREIGN KEY($FOLLOWED_PLAYLIST_USER_ID) REFERENCES $TABLE_USER($USER_ID),"
                + "FOREIGN KEY($FOLLOWED_PLAYLIST_ID) REFERENCES $TABLE_PLAYLIST($PLAYLIST_ID))")

        val createFollowedAlbumsTable = ("CREATE TABLE $TABLE_FOLLOWED_ALBUMS ("
                + "$FOLLOWED_ALBUM_USER_ID TEXT,"
                + "$FOLLOWED_ALBUM_ID TEXT,"
                + "PRIMARY KEY($FOLLOWED_ALBUM_USER_ID, $FOLLOWED_ALBUM_ID),"
                + "FOREIGN KEY($FOLLOWED_ALBUM_USER_ID) REFERENCES $TABLE_USER($USER_ID),"
                + "FOREIGN KEY($FOLLOWED_ALBUM_ID) REFERENCES $TABLE_ALBUM($ALBUM_ID))")

        val createHistoryTable = ("CREATE TABLE $TABLE_HISTORY ("
                + "$HISTORY_USER_ID TEXT,"
                + "$HISTORY_TRACK_ID TEXT,"
                + "$HISTORY_PLAYLIST_ID TEXT,"
                + "$HISTORY_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "PRIMARY KEY($HISTORY_USER_ID, $HISTORY_TRACK_ID, $HISTORY_TIMESTAMP),"
                + "FOREIGN KEY($HISTORY_USER_ID) REFERENCES $TABLE_USER($USER_ID),"
                + "FOREIGN KEY($HISTORY_TRACK_ID) REFERENCES $TABLE_TRACK($TRACK_ID),"
                + "FOREIGN KEY($HISTORY_PLAYLIST_ID) REFERENCES $TABLE_PLAYLIST($PLAYLIST_ID))")


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
        db.execSQL(createFollowedAlbumsTable)
        db.execSQL(createHistoryTable) //History
    }



    */

    init {
        // database is copied from assets
        DatabaseUtils.copyDatabaseIfNeeded(context)
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Not needed
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
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FOLLOWED_PLAYLISTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FOLLOWED_ALBUMS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FOLLOWER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        DatabaseUtils.copyDatabaseIfNeeded(context)
        Log.d("DATABASE", "Upgrade")
        
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        return SQLiteDatabase.openDatabase(
            context.getDatabasePath(DATABASE_NAME).path,
            null,
            SQLiteDatabase.OPEN_READWRITE
        )
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        return getReadableDatabase()
    }

    private fun deleteAllData(tableName: String, db: SQLiteDatabase) {
        val deleteQuery = "DELETE FROM $tableName"
        db.execSQL(deleteQuery)

        Log.d("DATABASE", "Successfully delete data")
    }

    // CRUD Operations for Users
    fun addUser(user: User): String {
        val db = this.writableDatabase
        val query = "SELECT 1 FROM $TABLE_USER WHERE $USER_ID = ?"

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


    fun getUserId(email: String, password: String): String? {
        val db = this.readableDatabase
        val selectQuery = "SELECT $USER_ID FROM $TABLE_USER WHERE $USER_EMAIL = ? AND $USER_PASSWORD = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(email, password))

        if (cursor.moveToFirst()) {
            val userId = cursor.getString(cursor.getColumnIndexOrThrow(USER_ID))
            cursor.close()
            db.close()
            return userId
        }

        cursor.close()
        db.close()
        return null
    }




    // CRUD Operations for Artist
    fun addArtist(artist: Artist): String {
        val db = this.writableDatabase
        val query = "SELECT 1 FROM $TABLE_ARTIST WHERE $ARTIST_ID = ?"

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

        if(!cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return null
        } //Check if there is no album

        do {
            allId.add(cursor.getString(columnIndex))
            counter++
        } while (cursor.moveToNext())

        cursor.close()
        db.close()
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

    fun getArtistGenres(artistId: String): List<String> {
        val genres = mutableListOf<String>()
        val db = this.readableDatabase
        val query = "SELECT $ARTIST_GENRE FROM $TABLE_ARTIST WHERE $ARTIST_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(artistId))

        if (cursor.moveToFirst()) {
            do {
                val genre = cursor.getString(cursor.getColumnIndexOrThrow(ARTIST_GENRE))
                genres.add(genre)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return genres
    }

    fun getAllGenres(): List<String> {
        val genres = mutableListOf<String>()
        val db = this.readableDatabase
        // SQL query to get genres ordered by the number of songs in descending order
        val query = """
        SELECT $ARTIST_GENRE, COUNT(*) AS song_count
        FROM $TABLE_ARTIST
        GROUP BY $ARTIST_GENRE
        ORDER BY song_count DESC
    """
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val genre = cursor.getString(cursor.getColumnIndexOrThrow(ARTIST_GENRE))
                genres.add(genre)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return genres
    }

    fun getTracksByArtist(artistId: String): List<Track> {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_TRACK,
            arrayOf(TRACK_ID, TRACK_ALBUM_ID, TRACK_NAME, TRACK_DURATION, TRACK_PATH),
            "$TRACK_ALBUM_ID IN (SELECT $ALBUM_ID FROM $TABLE_ALBUM WHERE $ALBUM_ARTIST_ID=?)",
            arrayOf(artistId),
            null,
            null,
            null
        )

        val tracks = mutableListOf<Track>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val track = Track(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
                tracks.add(track)
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return tracks
    }


    fun updateArtist(artist: Artist) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(ARTIST_NAME, artist.name)
            put(ARTIST_GENRE, artist.genre)
            put(ARTIST_IMAGE, artist.image)
        }
        db.update(TABLE_ARTIST, values, "$ARTIST_ID=?", arrayOf(artist.artistId))
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
        val query = "SELECT $ALBUM_ID FROM $TABLE_ALBUM WHERE $ALBUM_ID = ?"

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

        if(!cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return null
        } //Check if there is no album

        do {
            allId.add(cursor.getString(columnIndex))
            counter++
        } while (cursor.moveToNext())

        cursor.close()
        db.close()
        return allId
    }

    fun getAlbum(albumId: String): Album? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_ALBUM,
            arrayOf(ALBUM_ID, ALBUM_ARTIST_ID, ALBUM_NAME, ALBUM_RELEASE_DATE, ALBUM_IMAGE),
            "$ALBUM_ID=?",
            arrayOf(albumId),
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

    fun getAlbumIdFromTrackId(trackId: String): String? {
        val db = this.readableDatabase
        var albumId: String? = null

        // Query to get the album ID for the given track ID
        val query = "SELECT $TRACK_ALBUM_ID FROM $TABLE_TRACK WHERE $TRACK_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(trackId))

        // If the track exists, get the album ID
        if (cursor.moveToFirst()) {
            albumId = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_ALBUM_ID))
        }

        cursor.close()
        db.close()
        return albumId
    }

    fun getAlbumsByArtistId(artistId: String): List<Album> {
        val albumsList = ArrayList<Album>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_ALBUM,
            arrayOf(ALBUM_ID, ALBUM_ARTIST_ID, ALBUM_NAME, ALBUM_RELEASE_DATE, ALBUM_IMAGE),
            "$ALBUM_ARTIST_ID=?",
            arrayOf(artistId),
            null,
            null,
            "$ALBUM_NAME ASC" // Optional: Sort by album name
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val album = Album(
                    cursor.getString(0), // Album ID
                    cursor.getString(1), // Artist ID
                    cursor.getString(2), // Album Name
                    cursor.getString(3), // Release Date
                    cursor.getString(4)  // Album Image
                )
                albumsList.add(album)
            } while (cursor.moveToNext())
            cursor.close()
        }
        return albumsList
    }


    fun updateAlbum(album: Album) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(ALBUM_ARTIST_ID, album.artistId)
            put(ALBUM_NAME, album.name)
            put(ALBUM_RELEASE_DATE, album.releaseDate)
            put(ALBUM_IMAGE, album.image)
        }
        db.update(TABLE_ALBUM, values, "$ALBUM_ID=?", arrayOf(album.albumId))
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
        val query = "SELECT 1 FROM $TABLE_TRACK WHERE $TRACK_ID = ?"

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


    fun getTrack(trackId: String): Track? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_TRACK,
            arrayOf(TRACK_ID, TRACK_ALBUM_ID, TRACK_NAME, TRACK_DURATION, TRACK_PATH),
            "$TRACK_ID=?",
            arrayOf(trackId),
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
        db.update(TABLE_TRACK, values, "$TRACK_ID=?", arrayOf(track.trackId))
        db.close()
    }

    fun deleteTrack(trackId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_TRACK, "$TRACK_ID=?", arrayOf(trackId.toString()))
        db.close()
    }


    //get tracks function

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
        val cursor = db.rawQuery(query, arrayOf(playlistId))

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val trackId = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_ID))
                val albumId = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_ALBUM_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_NAME))
                val duration = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_DURATION))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_PATH))
                println("Success: $playlistId $name")
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

    fun getTop5TracksByArtist(artistId: String): List<Track> {
        val db = this.readableDatabase
        val trackList = mutableListOf<Track>()

        val cursor = db.query(
            "$TABLE_TRACK INNER JOIN $TABLE_ALBUM ON $TABLE_TRACK.$TRACK_ALBUM_ID=$TABLE_ALBUM.$ALBUM_ID",
            arrayOf(
                "$TABLE_TRACK.$TRACK_ID",
                "$TABLE_TRACK.$TRACK_NAME AS track_name", // Rename to avoid ambiguity
                "$TABLE_TRACK.$TRACK_DURATION",
                "$TABLE_TRACK.$TRACK_PATH",
                "$TABLE_TRACK.$TRACK_ALBUM_ID"
            ),
            "$TABLE_ALBUM.$ALBUM_ARTIST_ID=?",
            arrayOf(artistId),
            null,
            null,
            "$TABLE_TRACK.$TRACK_NAME ASC", // Order alphabetically by track name
            "5" // Limit to 5 tracks
        )


        if (cursor.moveToFirst()) {
            do {
                val track = Track(
                    cursor.getString(0),
                    cursor.getString(4),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
                )
                trackList.add(track)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return trackList
    }



    // CRUD Operations for Playlists
    fun addPlaylist(playlist: Playlist): String {
        val db = this.writableDatabase
        val query = "SELECT 1 FROM $TABLE_PLAYLIST WHERE $PLAYLIST_ID = ?"

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
        val query =
            "SELECT DISTINCT $PLAYLIST_ID FROM $TABLE_PLAYLIST WHERE $PLAYLIST_ID <> 'userLikedSong'"
        val cursor = db.rawQuery(query, arrayOf())
        val allId = ArrayList<String>()
        val columnIndex = cursor.getColumnIndexOrThrow(PLAYLIST_ID)
        var counter = 0

        if(!cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return null
        } //Check if there is no album

        do {
            allId.add(cursor.getString(columnIndex))
            counter++
        } while (cursor.moveToNext())

        cursor.close()
        db.close()
        return allId
    }

    fun getPlaylist(playlistId: String): Playlist? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PLAYLIST,
            arrayOf(PLAYLIST_ID, PLAYLIST_USER_ID, PLAYLIST_NAME, PLAYLIST_IMAGE),
            "$PLAYLIST_ID=?",
            arrayOf(playlistId),
            null,
            null,
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getString(0)
            val userId = cursor.getString(1)
            val name = cursor.getString(2)
            val image = cursor.getString(3)

            // Log values to see what the cursor is returning
            Log.d("DatabaseDebug", "Playlist ID: $id")
            Log.d("DatabaseDebug", "User ID: $userId")
            Log.d("DatabaseDebug", "Playlist Name: $name")
            Log.d("DatabaseDebug", "Playlist Image: $image")

            if (id != null && userId != null && name != null && image != null) {
                val playlist = Playlist(
                    id,
                    userId,
                    name,
                    image
                )
                cursor.close()
                playlist
            } else {
                Log.e("DatabaseDebug", "One of the fields is null")
                cursor.close()
                null
            }
        } else {
            Log.e("DatabaseDebug", "Cursor is null or no result found for playlistId: $playlistId")
            cursor?.close()
            null
        }
    }

    // New method to get the count of playlists by a specific user
    fun getPlaylistCountByUser(userId: String): Int {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_PLAYLIST WHERE $PLAYLIST_USER_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId))

        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()

        return count
    }


    fun getPlaylistIdByName(playlistName: String, userId: String): String? {
        val db = this.readableDatabase
        var playlistId: String? = null

        // Query to select the playlist ID where the name matches
        val query = "SELECT $PLAYLIST_ID FROM $TABLE_PLAYLIST WHERE $PLAYLIST_NAME = ? AND $PLAYLIST_USER_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(playlistName, userId))

        // If the playlist exists, retrieve the playlist ID
        if (cursor.moveToFirst()) {
            playlistId = cursor.getString(cursor.getColumnIndexOrThrow(PLAYLIST_ID))
        }

        cursor.close()
        db.close()
        return playlistId
    }


    fun updatePlaylist(playlist: Playlist) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(PLAYLIST_USER_ID, playlist.userId)
            put(PLAYLIST_NAME, playlist.name)
            put(PLAYLIST_IMAGE, playlist.image)
        }
        db.update(TABLE_PLAYLIST, values, "$PLAYLIST_ID=?", arrayOf(playlist.playlistId))
        db.close()
    }

    fun deletePlaylist(playlistId: String) : Boolean {
        val db = this.writableDatabase
        return try {
            val result = db.delete(TABLE_PLAYLIST, "$PLAYLIST_ID=?", arrayOf(playlistId.toString()))
            db.close()
            result > 0  // deletion was successful
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }

    //LikedSong -> outdated, to be removed
    /*fun addUserLikedSongsPlaylist(userId: String) {
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
*/
    //new liked song functions:
    fun getUserLikedTracks(userId: String): List<Track> {
        val likedTracks = mutableListOf<Track>()
        val db = this.readableDatabase

        // fetch liked tracks for the given userId
        val query = """
            SELECT * FROM $TABLE_TRACK 
            WHERE $TRACK_ID IN (
                SELECT $LIKE_TRACK_ID FROM "$TABLE_LIKE" WHERE $LIKE_USER_ID = ?
            )
        """

        val cursor = db.rawQuery(query, arrayOf(userId))

        if (cursor.moveToFirst()) {
            do {
                val trackId = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_ID))
                val albumId = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_ALBUM_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_NAME))
                val duration = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_DURATION))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_PATH))

                val track = Track(trackId, albumId, name, duration, path)
                likedTracks.add(track)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return likedTracks
    }
    fun isTrackLiked(trackId: String, userId: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM " + TABLE_LIKE +
                    " WHERE $LIKE_USER_ID = ? AND $LIKE_TRACK_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId, trackId))

        val isLiked = cursor.count > 0
        cursor.close()
        db.close()

        return isLiked
    }



    // CRUD Operations for Playlist_Tracks
    fun addPlaylistTrack(playlistTrack: PlaylistTrack) {
        val db = this.writableDatabase
        val query = "SELECT 1 FROM $TABLE_PLAYLIST_TRACK WHERE $PLAYLIST_TRACK_PLAYLIST_ID = ? AND $PLAYLIST_TRACK_TRACK_ID = ?"

        //Make sure that playlist track does exist
        val cursor = db.rawQuery(query, arrayOf(playlistTrack.playlistId, playlistTrack.trackId))
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
        return try {
            // Check if the track is already in the playlist
            val query = "SELECT * FROM $TABLE_PLAYLIST_TRACK WHERE $PLAYLIST_TRACK_PLAYLIST_ID = ? AND $PLAYLIST_TRACK_TRACK_ID = ?"
            val cursor = db.rawQuery(query, arrayOf(playlistId, trackId))

            val trackExists = cursor.count > 0
            cursor.close()

            // If the track does not exist, insert it
            if (!trackExists) {
                val contentValues = ContentValues().apply {
                    put(PLAYLIST_TRACK_PLAYLIST_ID, playlistId)
                    put(PLAYLIST_TRACK_TRACK_ID, trackId)
                }

                val result = db.insert(TABLE_PLAYLIST_TRACK, null, contentValues)
                db.close()
                result != -1L  // Return true if insertion is successful
            } else {
                db.close()
                false  // Track already exists, no insertion needed
            }
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
            arrayOf(playlistTrack.playlistId, playlistTrack.trackId)
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

    // CRUD Operations for Followers (artist)
    fun addFollower(follower: Follower) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(FOLLOWER_USER_ID, follower.userId)
            put(FOLLOWER_ARTIST_ID, follower.artistId)
        }
        db.insert(TABLE_FOLLOWER, null, values)
        db.close()
    }

    fun getUserLibraryArtists(userId: String): ArrayList<Artist> {
        val artists = mutableListOf<Artist>()
        val db = this.readableDatabase

        // Query for playlists created by the user
        // Join both table and compare with userId, if Followed_userId is UserId, then we show
        val query = "SELECT A.$ARTIST_ID, A.$ARTIST_NAME, A.$ARTIST_GENRE, A.$ARTIST_IMAGE " +
                    "FROM $TABLE_FOLLOWER AS FA " +
                    "JOIN $TABLE_ARTIST AS A ON A.$ARTIST_ID = FA.$FOLLOWER_ARTIST_ID " +
                    "WHERE $FOLLOWER_USER_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId))

        //If the cursor is empty
        if(!cursor.moveToFirst()) {
            Log.d("MusicAppDatabaseHelper", "No artist found")
            cursor.close()
            db.close()
            return artists.toCollection(ArrayList())
        }

        do {
            artists.add(Artist(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
            ))
        } while (cursor.moveToNext())

        cursor.close()
        db.close()
        return artists.toCollection(ArrayList())
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

    fun isArtistFollowed(userId: String, artistId: String): Boolean {
        val db = this.readableDatabase

        val query = "SELECT 1 FROM $TABLE_FOLLOWER WHERE $FOLLOWER_USER_ID = ? AND $FOLLOWER_ARTIST_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId, artistId))

        val isFollowed = cursor.count > 0  // the album is followed

        cursor?.close()
        db.close()

        return isFollowed
    }

    fun followArtist(userId: String, artistId: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(FOLLOWER_USER_ID, userId)
            put(FOLLOWER_ARTIST_ID, artistId)
        }

        val result = db.insert(TABLE_FOLLOWER, null, contentValues)
        db.close()

        return result != -1L  // Return true if insert was successful
    }

    fun unfollowArtist(userId: String, artistId: String): Boolean {
        val db = this.writableDatabase

        val result = db.delete(
            TABLE_FOLLOWER,
            "$FOLLOWER_USER_ID = ? AND $FOLLOWER_ARTIST_ID = ?",
            arrayOf(userId, artistId)
        )
        db.close()

        return result > 0  // Return true if delete was successful
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


    // CRUD Operations for Likes (tracks)
    fun addLike(like: Like) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(LIKE_USER_ID, like.userId)
            put(LIKE_TRACK_ID, like.trackId)
        }
        db.insert(TABLE_LIKE, null, values)
        db.close()
    }
    fun addLike(userId: String, trackId: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(LIKE_USER_ID, userId)
            put(LIKE_TRACK_ID, trackId)
        }
        db.insert(TABLE_LIKE, null, values)
        db.close()
    }

    fun getLike(userId: String, trackId: String): Like? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_LIKE,
            arrayOf(LIKE_USER_ID, LIKE_TRACK_ID),
            "$LIKE_USER_ID=? AND $LIKE_TRACK_ID=?",
            arrayOf(userId, trackId),
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

    fun deleteLike(userId: String, trackId: String) {
        val db = this.writableDatabase
        db.delete(
            TABLE_LIKE,
            "$LIKE_USER_ID=? AND $LIKE_TRACK_ID=?",
            arrayOf(userId, trackId)
        )
        db.close()
    }

    //FOLLOWED PLAYLIST

    fun followPlaylist(userId: String, playlistId: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(FOLLOWED_PLAYLIST_USER_ID, userId)
            put(FOLLOWED_PLAYLIST_ID, playlistId)
        }
        db.insert(TABLE_FOLLOWED_PLAYLISTS, null, values)
        db.close()
    }

    fun unfollowPlaylist(userId: String, playlistId: String) {
        val db = this.writableDatabase
        db.delete(TABLE_FOLLOWED_PLAYLISTS, "$FOLLOWED_PLAYLIST_USER_ID = ? AND $FOLLOWED_PLAYLIST_ID = ?", arrayOf(userId, playlistId))
        db.close()
    }

    fun isPlaylistFollowed(userId: String, playlistId: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_FOLLOWED_PLAYLISTS WHERE $FOLLOWED_PLAYLIST_USER_ID = ? AND $FOLLOWED_PLAYLIST_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId, playlistId))

        val isFollowed = cursor.count > 0  // the playlist is followed

        cursor?.close()
        db.close()

        return isFollowed
    }

    fun getUserLibraryPlaylists(userId: String): ArrayList<Playlist> {
        val playlists = mutableListOf<Playlist>()
        val db = this.readableDatabase

        // Query for playlists created by the user
        // Join both table and compare with userId, if one of PlaylistUserId or Followed_userId is UserId, then we insert
        val query = "SELECT P.$PLAYLIST_ID, P.$PLAYLIST_USER_ID, P.$PLAYLIST_NAME, P.$PLAYLIST_IMAGE " +
                    "FROM $TABLE_PLAYLIST AS P " +
                    "LEFT JOIN $TABLE_FOLLOWED_PLAYLISTS AS FP ON P.$PLAYLIST_ID = FP.$FOLLOWED_PLAYLIST_ID " +
                    "WHERE $FOLLOWED_PLAYLIST_USER_ID = ? OR $PLAYLIST_USER_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId, userId))

        //If the cursor is empty
        if(!cursor.moveToFirst()) {
            Log.d("MusicAppDatabaseHelper", "No playlist found")
            cursor.close()
            db.close()
            return playlists.toCollection(ArrayList())
        }

        do {
            playlists.add(Playlist(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
            ))
        } while (cursor.moveToNext())

        cursor.close()
        db.close()
        return playlists.toCollection(ArrayList())
    }


    //FOLLOWED ALBUMS
    fun isAlbumFollowed(userId: String, albumId: String): Boolean {
        val db = this.readableDatabase

        val query = "SELECT 1 FROM $TABLE_FOLLOWED_ALBUMS WHERE $FOLLOWED_ALBUM_USER_ID = ? AND $FOLLOWED_ALBUM_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId, albumId))

        val isFollowed = cursor.count > 0  // the album is followed

        cursor?.close()
        db.close()

        return isFollowed
    }
    fun followAlbum(userId: String, albumId: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(FOLLOWED_ALBUM_USER_ID, userId)
            put(FOLLOWED_ALBUM_ID, albumId)
        }

        val result = db.insert(TABLE_FOLLOWED_ALBUMS, null, contentValues)
        db.close()

        return result != -1L  // Return true if insert was successful
    }

    fun unfollowAlbum(userId: String, albumId: String): Boolean {
        val db = this.writableDatabase

        val result = db.delete(
            TABLE_FOLLOWED_ALBUMS,
            "$FOLLOWED_ALBUM_USER_ID = ? AND $FOLLOWED_ALBUM_ID = ?",
            arrayOf(userId, albumId)
        )
        db.close()

        return result > 0  // Return true if delete was successful
    }

    fun getUserFollowedAlbums(userId: String): ArrayList<Album> {
        val db = this.readableDatabase
        val followedAlbums = mutableListOf<Album>()

        val query = "SELECT * FROM $TABLE_ALBUM WHERE $ALBUM_ID IN (" +
                "SELECT $FOLLOWED_ALBUM_ID FROM $TABLE_FOLLOWED_ALBUMS WHERE $FOLLOWED_ALBUM_USER_ID = ?)"

        val cursor = db.rawQuery(query, arrayOf(userId))

        if (cursor.moveToFirst()) {
            do {
                val album = Album(
                    albumId = cursor.getString(cursor.getColumnIndexOrThrow(ALBUM_ID)),
                    artistId = cursor.getString(cursor.getColumnIndexOrThrow(ALBUM_ARTIST_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(ALBUM_NAME)),
                    releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(ALBUM_RELEASE_DATE)),
                    image = cursor.getString(cursor.getColumnIndexOrThrow(ALBUM_IMAGE))
                )
                followedAlbums.add(album)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return followedAlbums.toCollection(ArrayList())
    }

    fun addGenrePlaylistIfNotExists(playlistName: String, genre: String, userId: String) {
        val db = this.writableDatabase

        // Check if the playlist already exists
        val queryCheck = "SELECT $PLAYLIST_ID FROM $TABLE_PLAYLIST WHERE $PLAYLIST_NAME = ? AND $PLAYLIST_USER_ID = ?"
        val cursorCheck = db.rawQuery(queryCheck, arrayOf(playlistName, userId))

        var playlistId: String? = null
        if (cursorCheck.moveToFirst()) {
            // Playlist already exists, get its ID
            playlistId = cursorCheck.getString(cursorCheck.getColumnIndexOrThrow(PLAYLIST_ID))
            cursorCheck.close()

            // Check if the playlist already has tracks
            val queryCheckTracks = "SELECT 1 FROM $TABLE_PLAYLIST_TRACK WHERE $PLAYLIST_TRACK_PLAYLIST_ID = ?"
            val cursorCheckTracks = db.rawQuery(queryCheckTracks, arrayOf(playlistId))

            if (cursorCheckTracks.moveToFirst()) {
                // Playlist already has tracks, no need to add tracks again
                println("Playlist already exists and has tracks")
                cursorCheckTracks.close()
                db.close()
                return
            }
            cursorCheckTracks.close()
        } else {
            // Playlist does not exist, create a new one
            playlistId = UUID.randomUUID().toString() // Generate a unique ID for the new playlist
            val playlistValues = ContentValues().apply {
                put(PLAYLIST_ID, playlistId)
                put(PLAYLIST_USER_ID, userId)
                put(PLAYLIST_NAME, playlistName)
                put(PLAYLIST_IMAGE, "123") // Set playlist image if available
            }
            db.insert(TABLE_PLAYLIST, null, playlistValues)
        }

        // Get all tracks for the given genre using the new function
        val tracks = getAllTracksByGenre(genre)

        // Add each track to the playlist using addPlaylistTrack function
        tracks.forEach { trackId ->
            addPlaylistTrack(playlistId!!, trackId)
        }

        db.close()
    }

    fun getAllTracksByGenre(genre: String): List<String> {
        val db = this.readableDatabase
        val trackIds = mutableListOf<String>()

        // Query to retrieve all tracks for the given genre
        val query = """
        SELECT t.$TRACK_ID
        FROM $TABLE_TRACK t
        JOIN $TABLE_ALBUM a ON t.$TRACK_ALBUM_ID = a.$ALBUM_ID
        JOIN $TABLE_ARTIST ar ON a.$ALBUM_ARTIST_ID = ar.$ARTIST_ID
        WHERE ar.$ARTIST_GENRE = ?
    """
        val cursor = db.rawQuery(query, arrayOf(genre))

        if (cursor.moveToFirst()) {
            do {
                val trackId = cursor.getString(cursor.getColumnIndexOrThrow(TRACK_ID))
                trackIds.add(trackId)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return trackIds
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

    //Sort from A-Z
    /**
     * Sort
     *
     * Function takes three parameters type, order and curUserId
     *
     * @param type playlist, album, and artist
     * @param order ASC, DESC, ADDED. Don't need to be uppercase
     * @param curUserId the current user using the app
     *
     * @return an ArrayList of playlist/album/artist
     */
    fun sort(type: String, order: String, curUserId: String) : ArrayList<Any> {
        val rawQuery: String
        val list = ArrayList<Any>()
        val db = this.readableDatabase
        var tableName: String
        order.uppercase()
        var orderTmp = order

        //Make query

        when (type.lowercase()) {
            "album" -> {
                if(order == "ADDED") {
                    orderTmp = "DESC"
                    tableName = ALBUM_TIMESTAMP
                } else tableName = ALBUM_NAME
                rawQuery =  "SELECT DISTINCT A.$ALBUM_ID, A.$ALBUM_ARTIST_ID, A.$ALBUM_NAME, A.$ALBUM_RELEASE_DATE, A.$ALBUM_IMAGE " +
                            "FROM $TABLE_ALBUM AS A " +
                            "LEFT JOIN $TABLE_FOLLOWED_ALBUMS AS FA ON FA.$FOLLOWED_ALBUM_ID = A.$ALBUM_ID " +
                            "WHERE FA.$FOLLOWED_ALBUM_USER_ID = '$curUserId' " +
                            "ORDER BY $tableName $orderTmp"
            }

            "artist" -> {
                if(order == "ADDED") {
                    orderTmp = "DESC"
                    tableName = ARTIST_TIMESTAMP
                } else tableName = ARTIST_NAME
                rawQuery =  "SELECT DISTINCT A.$ARTIST_ID, A.$ARTIST_NAME, A.$ARTIST_GENRE, A.$ARTIST_IMAGE " +
                            "FROM $TABLE_ARTIST AS A " +
                            "JOIN $TABLE_FOLLOWER AS FA ON A.$ARTIST_ID = FA.$FOLLOWER_ARTIST_ID " +
                            "WHERE FA.$FOLLOWER_USER_ID = '$curUserId' " +
                            "ORDER BY $tableName $orderTmp"
            }

            "playlist" -> {
                if(order == "ADDED") {
                    orderTmp = "DESC"
                    tableName = PLAYLIST_TIMESTAMP
                } else tableName = PLAYLIST_NAME
                rawQuery =  "SELECT DISTINCT P.$PLAYLIST_ID, P.$PLAYLIST_USER_ID, P.$PLAYLIST_NAME, P.$PLAYLIST_IMAGE " +
                            "FROM $TABLE_PLAYLIST AS P " +
                            "LEFT JOIN $TABLE_FOLLOWED_PLAYLISTS AS FP ON FP.$FOLLOWED_PLAYLIST_ID = P.$PLAYLIST_ID " +
                            "WHERE P.$PLAYLIST_USER_ID = '$curUserId' OR FP.$FOLLOWED_PLAYLIST_USER_ID = '$curUserId' " +
                            "ORDER BY $tableName $orderTmp"
            }
            else -> {
                Log.e("MusicAppDatabaseHelper", "Wrong type")
                db.close()
                return list
            }
        }

        val cursor = db.rawQuery(rawQuery, arrayOf())
        if(!cursor.moveToFirst()) {
            cursor.close()
            db.close()
            Log.e("MusicAppDatabaseHelper", "No element to sort")
            return list
        }

        //Add to a list and return
        when(type.lowercase()) {
            "album" -> {
                do {
                    list.add(Album(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4)))
                } while (cursor.moveToNext())
            }

            "artist" -> {
                do {
                    list.add(Artist(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3)))
                } while (cursor.moveToNext())
            }

            "playlist" -> {
                do {
                    list.add(Playlist(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3)))
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()
        return list
    }

    /**
     * Getting recently played
     *
     * Function takes two parameters type and curUserId
     *
     * @param type playlist, album, artist, and track
     * @param curUserId the current user using the app
     *
     * @return an ArrayList of playlist/album/artist/track
     */
    fun getNewest(type: String, curUserId: String) : ArrayList<Any> {
        val rawQuery: String
        val list = ArrayList<Any>()
        val db = this.readableDatabase

        //Make query

        when (type.lowercase()) {
            "album" -> {
                rawQuery =  "SELECT DISTINCT A.$ALBUM_ID, A.$ALBUM_ARTIST_ID, A.$ALBUM_NAME, A.$ALBUM_RELEASE_DATE, A.$ALBUM_IMAGE " +
                        "FROM $TABLE_HISTORY AS H " +
                        "JOIN $TABLE_TRACK AS T ON H.$HISTORY_TRACK_ID = T.$TRACK_ID " +
                        "JOIN $TABLE_ALBUM AS A ON T.$TRACK_ALBUM_ID = A.$ALBUM_ID " +
                        "WHERE H.$HISTORY_USER_ID = '$curUserId' " +
                        "ORDER BY $HISTORY_TIMESTAMP DESC"
            }

            "artist" -> {
                rawQuery =  "SELECT DISTINCT ART.$ARTIST_ID, ART.$ARTIST_NAME, ART.$ARTIST_GENRE, ART.$ARTIST_IMAGE " +
                        "FROM $TABLE_HISTORY AS H " +
                        "JOIN $TABLE_TRACK AS T ON H.$HISTORY_TRACK_ID = T.$TRACK_ID " +
                        "JOIN $TABLE_ALBUM AS A ON T.$TRACK_ALBUM_ID = A.$ALBUM_ID " +
                        "JOIN $TABLE_ARTIST AS ART ON A.$ALBUM_ARTIST_ID = ART.$ARTIST_ID"
                        "WHERE H.$HISTORY_USER_ID = '$curUserId' " +
                        "ORDER BY $HISTORY_TIMESTAMP DESC"
            }

            "playlist" -> {
                rawQuery = "SELECT DISTINCT P.$PLAYLIST_ID, P.$PLAYLIST_USER_ID, P.$PLAYLIST_NAME, P.$PLAYLIST_IMAGE " +
                        "FROM $TABLE_HISTORY AS H " +
                        "JOIN $TABLE_PLAYLIST_TRACK AS PT ON H.$HISTORY_TRACK_ID = PT.$PLAYLIST_TRACK_TRACK_ID " +
                        "JOIN $TABLE_PLAYLIST AS P ON PT.$PLAYLIST_TRACK_PLAYLIST_ID = P.$PLAYLIST_ID " +
                        "WHERE H.$HISTORY_USER_ID = '$curUserId' AND H.$HISTORY_PLAYLIST_ID = P.$PLAYLIST_ID " +
                        "ORDER BY $HISTORY_TIMESTAMP DESC"
            }
            "track" -> {
                rawQuery =  "SELECT DISTINCT T.$TRACK_ID, T.$TRACK_ALBUM_ID, T.$TRACK_NAME, T.$TRACK_DURATION, T.$TRACK_PATH " +
                        "FROM $TABLE_HISTORY AS H " +
                        "JOIN $TABLE_TRACK AS T ON H.$HISTORY_TRACK_ID = T.$TRACK_ID " +
                        "WHERE H.$HISTORY_USER_ID = '$curUserId' " +
                        "ORDER BY $HISTORY_TIMESTAMP DESC"
            }
            else -> {
                Log.e("MusicAppDatabaseHelper", "Wrong type")
                db.close()
                return list
            }
        }

        val cursor = db.rawQuery(rawQuery, arrayOf())
        if(!cursor.moveToFirst()) {
            cursor.close()
            db.close()
            Log.e("MusicAppDatabaseHelper", "No element to sort")
            return list
        }

        //Add to a list and return
        when(type.lowercase()) {
            "album" -> {
                do {
                    list.add(Album(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4)))
                } while (cursor.moveToNext())
            }

            "artist" -> {
                do {
                    list.add(Artist(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3)))
                } while (cursor.moveToNext())
            }

            "playlist" -> {
                do {
                    list.add(Playlist(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3)))
                } while (cursor.moveToNext())
            }
            "track" -> {
                do {
                    list.add(Track(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4)))
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()
        return list
    }

    //Add history
    /**
     * Automatically record all listening activity by providing user_id, playlist_id, track_id
     * @param curUserId the user who listened to the track (Normally the current one)
     * @param playlistId the playlist where the track belongs. Providing "" if no playlist found
     * @param trackId the id of the track
     * @return true if successfully added
     */
    fun addHistory(curUserId: String, playlistId: String, trackId: String) : Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        try {
            db.beginTransaction()
            val historyValue = ContentValues().apply {
                put(HISTORY_USER_ID, curUserId)
                put(HISTORY_PLAYLIST_ID, playlistId)
                put(HISTORY_TRACK_ID, trackId)
            }
            db.insert(TABLE_HISTORY, null, historyValue)

            db.setTransactionSuccessful()
            Log.d("MusicAppDatabaseHelper",
                "User $curUserId, Playlist $playlistId, Track $trackId recorded")
        }
        catch (e: Exception) {
            Log.e("MusicAppDatabaseHelper", "Can't record - ", e)
            db.close()
            return false
        }
        finally {
            db.endTransaction()
        }

        db.close()
        return true
    }

    ///delete all

    fun deleteHistory() {
        val db = this.writableDatabase
        deleteAllData(TABLE_HISTORY, db)
        db.close()
    }
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

    fun exportDatabaseToFile(): Boolean {
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        val exportFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            DATABASE_NAME
        )

        try {
            FileInputStream(dbFile).use { input ->
                FileOutputStream(exportFile).use { output ->
                    input.copyTo(output)
                }
            }
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * Import the database from a file in external storage.
     * @return true if import is successful, false otherwise.
     */
    fun importDatabaseFromFile(): Boolean {
        val importFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            DATABASE_NAME
        )
        val dbFile = context.getDatabasePath(DATABASE_NAME)

        if (!importFile.exists()) {
            return false
        }

        try {
            FileInputStream(importFile).use { input ->
                FileOutputStream(dbFile).use { output ->
                    input.copyTo(output)
                }
            }
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }


}

