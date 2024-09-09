package com.example.mymusicapp.models

import android.os.Parcel
import android.os.Parcelable

data class Track(
    val trackId: String,
    val albumId: String,
    val name: String,
    val duration: String, // Duration stored as string in format "MM:SS"
    var path: String = "https://stream.nct.vn/Unv_Audio407/Style-TaylorSwift-12613800.mp3?st=HQN3Vju0fcNjatL9jDvorQ&e=1726036277&a=1&p=0&r=a0dad11b4823361cef20dccf5ac0758a&t=1725435123477"     // Default audio file path
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackId)
        parcel.writeString(albumId)
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
