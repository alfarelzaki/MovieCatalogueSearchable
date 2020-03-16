package com.nubdev.consumerfavoritemovie.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.nubdev.moviecataloguesearchable"
    const val SCHEME = "content"

    internal class MoviesColumns : BaseColumns {

        companion object {
            const val TABLE_NAME_MOVIE = "movie"
            const val TABLE_NAME_TVSHOW = "tvShow"
            const val _ID = "_id"
            const val TITLE = "title"
            const val OVERVIEW = "overview"
            const val DATE = "date"
            const val POSTER = "poster"

            val CONTENT_URI_MOVIE: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME_MOVIE)
                .build()

            val CONTENT_URI_TV: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME_TVSHOW)
                .build()
        }

    }

}

