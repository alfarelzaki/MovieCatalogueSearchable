package com.nubdev.consumerfavoritemovie.dataclass

import android.os.Parcel
import android.os.Parcelable

data class Movie(
    var title: String?,
    var overview: String?,
    var poster: String?,
    var year: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    constructor() : this(
        "", "", "", "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(overview)
        parcel.writeString(poster)
        parcel.writeString(year)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}