package com.example.mymusicapp.models

import android.os.Parcel
import android.os.Parcelable

data class Track(
    val trackId: Int,
    val albumId: Int,
    val name: String,
    val duration: String, // Duration stored as string in format "MM:SS"
    val path: String // Path to the audio file
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(trackId)
        parcel.writeInt(albumId)
        parcel.writeString(name)
        parcel.writeString(duration)
        parcel.writeString(path)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}
