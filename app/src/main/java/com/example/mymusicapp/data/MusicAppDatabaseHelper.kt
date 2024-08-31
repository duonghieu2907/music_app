package com.example.mymusicapp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.adamratzman.spotify.models.*
import android.content.ContentValues

class MusicAppDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "music_app.db"
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

        // Like Table
        private const val TABLE_LIKE = "Like"
        private const val LIKE_USER_ID = "user_id"
        private const val LIKE_TRACK_ID = "track_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = ("CREATE TABLE $TABLE_USER ("
                + "$USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$USER_NAME TEXT,"
                + "$USER_EMAIL TEXT,"
                + "$USER_PASSWORD TEXT,"
                + "$USER_DOB TEXT,"
                + "$USER_PROFILE_IMAGE TEXT)")

        val createArtistTable = ("CREATE TABLE $TABLE_ARTIST ("
                + "$ARTIST_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$ARTIST_NAME TEXT,"
                + "$ARTIST_GENRE TEXT,"
                + "$ARTIST_IMAGE TEXT)")

        val createAlbumTable = ("CREATE TABLE $TABLE_ALBUM ("
                + "$ALBUM_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$ALBUM_ARTIST_ID INTEGER,"
                + "$ALBUM_NAME TEXT,"
                + "$ALBUM_RELEASE_DATE TEXT,"
                + "$ALBUM_IMAGE TEXT,"
                + "FOREIGN KEY($ALBUM_ARTIST_ID) REFERENCES $TABLE_ARTIST($ARTIST_ID))")

        val createTrackTable = ("CREATE TABLE $TABLE_TRACK ("
                + "$TRACK_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$TRACK_ALBUM_ID INTEGER,"
                + "$TRACK_NAME TEXT,"
                + "$TRACK_DURATION TEXT,"
                + "$TRACK_PATH TEXT,"
                + "FOREIGN KEY($TRACK_ALBUM_ID) REFERENCES $TABLE_ALBUM($ALBUM_ID))")

        val createPlaylistTable = ("CREATE TABLE $TABLE_PLAYLIST ("
                + "$PLAYLIST_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$PLAYLIST_USER_ID INTEGER,"
                + "$PLAYLIST_NAME TEXT,"
                + "$PLAYLIST_IMAGE TEXT,"
                + "FOREIGN KEY($PLAYLIST_USER_ID) REFERENCES $TABLE_USER($USER_ID))")

        val createPlaylistTrackTable = ("CREATE TABLE $TABLE_PLAYLIST_TRACK ("
                + "$PLAYLIST_TRACK_PLAYLIST_ID INTEGER,"
                + "$PLAYLIST_TRACK_TRACK_ID INTEGER,"
                + "$PLAYLIST_TRACK_ORDER INTEGER,"
                + "PRIMARY KEY($PLAYLIST_TRACK_PLAYLIST_ID, $PLAYLIST_TRACK_TRACK_ID),"
                + "FOREIGN KEY($PLAYLIST_TRACK_PLAYLIST_ID) REFERENCES $TABLE_PLAYLIST($PLAYLIST_ID),"
                + "FOREIGN KEY($PLAYLIST_TRACK_TRACK_ID) REFERENCES $TABLE_TRACK($TRACK_ID))")

        val createFollowerTable = ("CREATE TABLE $TABLE_FOLLOWER ("
                + "$FOLLOWER_USER_ID INTEGER,"
                + "$FOLLOWER_ARTIST_ID INTEGER,"
                + "PRIMARY KEY($FOLLOWER_USER_ID, $FOLLOWER_ARTIST_ID),"
                + "FOREIGN KEY($FOLLOWER_USER_ID) REFERENCES $TABLE_USER($USER_ID),"
                + "FOREIGN KEY($FOLLOWER_ARTIST_ID) REFERENCES $TABLE_ARTIST($ARTIST_ID))")

        val createLikeTable = ("CREATE TABLE $TABLE_LIKE ("
                + "$LIKE_USER_ID INTEGER,"
                + "$LIKE_TRACK_ID INTEGER,"
                + "PRIMARY KEY($LIKE_USER_ID, $LIKE_TRACK_ID),"
                + "FOREIGN KEY($LIKE_USER_ID) REFERENCES $TABLE_USER($USER_ID),"
                + "FOREIGN KEY($LIKE_TRACK_ID) REFERENCES $TABLE_TRACK($TRACK_ID))")

        // Execute the SQL statements
        db.execSQL(createUserTable)
        db.execSQL(createArtistTable)
        db.execSQL(createAlbumTable)
        db.execSQL(createTrackTable)
        db.execSQL(createPlaylistTable)
        db.execSQL(createPlaylistTrackTable)
        db.execSQL(createFollowerTable)
        db.execSQL(createLikeTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //db.execSQL("DROP TABLE IF EXISTS $TABLE_LIKE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FOLLOWER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLAYLIST_TRACK")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLAYLIST")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRACK")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ALBUM")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ARTIST")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }



    fun addUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(USER_NAME, user.name)
            put(USER_EMAIL, user.email)
            put(USER_PASSWORD, user.password)
            put(USER_DOB, user.dateOfBirth)
            put(USER_PROFILE_IMAGE, user.profileImage)
        }
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    fun getUser(userId: Int): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USER,
            arrayOf(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD, USER_DOB, USER_PROFILE_IMAGE),
            "$USER_ID=?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val user = User(
                cursor.getInt(0),
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

    fun addArtist(artist: Artist) { /*...*/ }
    //fun getArtist(artistId: Int): Artist? { /*...*/ }
    fun updateArtist(artist: Artist) { /*...*/ }
    fun deleteArtist(artistId: Int) { /*...*/ }

    // Repeat for Album, Track, Playlist, Playlist_Track, Follower, Like
}

